package com.steven.pick_photo_album;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Arrays;

/**
 * 打开相册的方式
 *  * Intent.ACTION_PICK：默认打开图库，打开默认是缩略图界面，还需要进一步点开查看，通常用来选择指定URI下的资源。
 *  * Intent.ACTION_GET_CONTENT：4.4以下默认打开缩略图。4.4以上打开文件管理器供选择，选择图库打开为缩略图页面，选择图片打开为原图浏览，支持多选。
 *  * Intent.ACTION_OPEN_DOCUMENT（API19）：仅限4.4或以上使用默认打开原图，支持多选。
 * URI
 *  * 组成：【域名://主机名/路径/id】
 *  * case：
 *      1、从图片获取到的uri格式：content://com.android.providers.media.documents/document/image%3A35144【不可直接得到路径，需要进行转化】
 *      2、从图库获取的uri：content://media/external/images/media/35144
 *      3、通过ACTION_PICK方式得到的URI通常格式：content://media/external/images/media/35144
 *      4、通过ACTION_GET_CONTENT方式得到的URI通常为：
 *          content://media/extenral/images/media/17766
            content://com.android.providers.media.documents/document/image:2706
 *  转换问题——其中主机名可能为如下几种中的一种，直接通过URI查询ContentProvider，将会得到空路径，需要转换：
 *      1、com.android.externalstorage.documents
 *      2、com.android.providers.downloads.documents
 *      3、com.android.providers.media.documents
 *      PS: 注意必须申请READ_EXTERNAL_STORAGE权限，否则无法正确解析文件路径
 *  此类封装了通用解析uri的逻辑。
 */
public class UriUtils {
    private static final String TAG = "UriUtils# ";
    /**
     *
     * @param context
     * @param uri schema://host(authorization)/path/id
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        Log.d(TAG, "parse uri " + uri);
        int sdk_version = Build.VERSION.SDK_INT;
        if (sdk_version >= 19) {
            return getRealPathFromUriAboveApi19(context, uri);
        } else {
            return getRealPathFromUriBelowApi19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowApi19(Context context, Uri uri) {
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            data = getDataColumn(context, uri, null, null);
        }
        Log.d(TAG, "api<19, return img data " + data);
        return data;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @RequiresApi(21)
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            Log.d(TAG, " document# document id " + documentId);
            if (isMediaDocument(uri)) {
                final String[] divide = documentId.split(":");
                final String type = divide[0];
                Log.d(TAG, " document# document id divide" + Arrays.toString(divide) + ", type " + type);
                Uri mediaUri = null;
                if ("image".equals(type)) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {divide[1]};
                filePath = getDataColumn(context, mediaUri, selection, selectionArgs);
                getDataColumn(context, uri, mediaUri, selection, selectionArgs);
                Log.d(TAG, " document# meidia " + filePath);
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
                Log.d(TAG, " document# download " + filePath);
            } else if(isExternalStorageDocument(uri)) {
                String [] split = documentId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    if("primary".equalsIgnoreCase(type)) {
                        filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                Log.d(TAG, " document# external " + filePath);
            }
            Log.d(TAG, "uri is document " + filePath);
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
            Log.d(TAG, "uri is content " + filePath);
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
            Log.d(TAG, "uri is file " + filePath);
        }
        Log.d(TAG, "api>=19, return img path " + filePath);
        return filePath;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        Log.d(TAG, "parse database, uri " + uri);
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexOrThrow = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndexOrThrow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    private static String getDataColumn(Context context, Uri originUri, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = null;
        Log.d(TAG, "parse database, originUri " + originUri);
        try {
            cursor = context.getContentResolver().query(originUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexOrThrow = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndexOrThrow);
//                for (int i = 0; i < cursor.getColumnNames().length; i++) {
//                    String name = cursor.getColumnName(i);
//                    int columnIndexOrThrow = cursor.getColumnIndex(name);
//                }
                Log.d(TAG, "parse database, "+"path "+ path);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
}

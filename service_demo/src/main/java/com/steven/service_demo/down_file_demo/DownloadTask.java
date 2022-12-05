package com.steven.service_demo.down_file_demo;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 基于Service的文件下载Demo
 * AsyncTask<String, Integer, Integer>
 *     第一个泛型：表示指定任务的传入参数类型AsyncTask# doInBackground
 *     第二个泛型：表示使用Integer来表示任务进度AsyncTask# onProgressUpdate
 *     第三个泛型：表示使用Integer来表示任务结果AsyncTask# onPostExecute
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    /**
     * 状态枚举码
     */
    enum CODE_TYPE{
        SUCCESS(1),
        FAILED(2),
        PAUSED(3),
        CANCELED(4);
        int code;
        CODE_TYPE(Integer code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
        /**
         * switch-case语句要求常量表达式，需要重写枚举的getValue在编译时期确认返回常量
         */
        public static CODE_TYPE getValue(Integer code){
            for (CODE_TYPE codeType : CODE_TYPE.values()) {
                if (codeType.getCode() == code){
                    return codeType;
                }
            }
            return null;
        }
    }

    private DownloadListener listener; // 监听器
    private boolean isCanceled;
    private boolean isPaused;
    private int lastProgress;

    public DownloadTask(DownloadListener listener){
        this.listener = listener;
    }

    /**
     * 基于AsyncTask的后台线程执行任务
     * @param params：任务的传入参数
     * @return Integer：下载进度
     */
    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile saveFile = null;
        File file = null;
        try{
            long downloadedLength = 0; // 记录已下载的文件长度
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            // 外部存储的公共部分
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory+fileName);
            if (file.exists()){
                downloadedLength = file.length();
            }
            long contentLength= getContentLength(downloadUrl);
            if (contentLength == 0){
                return CODE_TYPE.FAILED.code;
            }else if (contentLength == downloadedLength){
                // 表明已经下载完成
                return CODE_TYPE.SUCCESS.code;
            }
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    // 开启断点下载，指定开始下载的字节起始位置。
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response != null){
                // 利用输入流读取repose的数据
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                // 跳过已经下载的字节数
                saveFile.seek(downloadedLength);
                byte[] bytes = new byte[1024];
                int total = 0;
                int len =0;
                while ((len = is.read(bytes))!=-1){
                    if (isCanceled){
                        return CODE_TYPE.CANCELED.code;
                    }else if (isPaused){
                        return CODE_TYPE.PAUSED.code;
                    }else {
                        total += len;
                        // 写入本地文件夹
                        saveFile.write(total);
                        // 计算已经下载的百分比
                        int progress = (int) ( (total + downloadedLength) * 100 / contentLength);
                        // 异步回调Async：更新下载进度
                        publishProgress(progress);
                    }
                }
                // 关闭流
                response.body().close();
                return CODE_TYPE.SUCCESS.code;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is!=null)
                    is.close();
                if (saveFile!=null)
                    saveFile.close();
                if (isCanceled && file !=null)
                    file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return CODE_TYPE.FAILED.code;
    }

    /**
     * 更新后台任务执行进度
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        // 更新下载进度
        int progress = values[0];
        if (progress>lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /**
     * 根据任务结果执行对应的操作
     * @param status
     */
    @Override
    protected void onPostExecute(Integer status) {
        switch (CODE_TYPE.getValue(status)){
            // case语句要求常量表达式，需要重写枚举的getValue在编译时期确认CODE
            case SUCCESS:
                listener.onSuccess();
                break;
            case FAILED:
                listener.onFailed();
                break;
            case CANCELED:
                listener.onCanceled();
                break;
            case PAUSED:
                listener.onPaused();
                break;
        }
    }

    public void pauseDownload(){
        isPaused = true;
    }

    public void cancelDownload(){
        isCanceled = true;
    }

    /**
     * 根据响应体返回数据的长度
     * @param downloadUrl
     * @return
     * @throws IOException
     */
    private long getContentLength(String downloadUrl) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response!=null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }
}

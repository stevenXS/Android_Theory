package com.steven.camera;

import android.media.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageSaver implements Runnable{
    /**
     * The JPEG image
     */
    private final Image mImage;
    /**
     * The file we save the image into.
     */
    private final File mFile;

    ImageSaver(Image image, File file) {
        mImage = image;
        mFile = file;
    }

    @Override
    public void run() {
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()]; // 开辟字节数组，buffer.remaining()返回该缓冲区的元素总数
        buffer.get(bytes); // 将buffer的数据传输到byte数组
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mFile);
            fos.write(bytes); // 从Byte数组写入文件流
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.mnysqtp.com.mnyproject.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 22428 on 2017/12/10.
 */

public class IOclass {
    public static byte[] read (String path) throws IOException{
        InputStream in = new FileInputStream(path);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    public static void write (FileOutputStream in, byte[] content) throws IOException{
        in.write(content, 0, content.length);
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    public static void copy(InputStream IS, String newpath) throws IOException{
        FileOutputStream fos = new FileOutputStream(newpath);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = IS.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
        }
        fos.flush();
        fos.close();
    }
}

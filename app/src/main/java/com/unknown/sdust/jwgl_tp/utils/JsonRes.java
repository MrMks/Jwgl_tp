package com.unknown.sdust.jwgl_tp.utils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonRes {
    public static String loginFile = "login.json";
    public static String tableFile = "table.json";
    public static String cookieFile = "cookie.json";
    public static String versionFile = "version.json";

    private static final Gson gson = new Gson();
    private static File path;
    public static void setPath(File file){
        path = file;
    }

    public static void write(Object obj, String child) {
        synchronized (gson){
            File file = new File(path,child);
            FileWriter writer;
            try {
                writer = new FileWriter(file);
                writer.write(gson.toJson(obj));
                writer.flush();
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static <T> T read(String child,Class<T> classOfT) throws IOException {
        synchronized (gson){
            T result = null;
            File file = new File(path,child);
            if(file.exists()){
                FileReader fileReader = new FileReader(file);
                result =  gson.fromJson(fileReader,classOfT);
                fileReader.close();
            }
            return result;
        }
    }

    public static boolean delete(String fileName){
        synchronized (gson){
            File file = new File(path,fileName);
            return file.delete();
        }
    }

    public static File getPath() {
        return path;
    }
}
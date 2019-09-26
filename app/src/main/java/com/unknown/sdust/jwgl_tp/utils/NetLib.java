package com.unknown.sdust.jwgl_tp.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NetLib {
    private static Map<String,String> baseHeaders;
    private static class MapGenerator<K,V> {
        private Map<K,V> map;
        MapGenerator(Map<K,V> map){
            this.map = map;
        }

        NetLib.MapGenerator<K,V> p(K key, V val){
            map.put(key, val);
            return this;
        }
    }

    static {
        baseHeaders = new HashMap<>();
        new MapGenerator<>(baseHeaders)
                .p("Accept-Encoding","gzip, deflate")
                .p("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .p("Cache-Control","max-age=0")
                .p("Connection","keep-alive")
                .p("DNT","1")
                .p("Host","jwgl.sdust.edu.cn")
                .p("Upgrade-Insecure-Requests","1")
                .p("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .p("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
    }

    private static Map<String,String> emptyMap = Collections.emptyMap();

    public static Connection.Response connect(String url, Connection.Method method) throws IOException {
        return connect(url, method, emptyMap);
    }

    public static Connection.Response connect(String url, Connection.Method method, Map<String,String> _header) throws IOException {
        return connect(url, method, _header, emptyMap);
    }

    public static Connection.Response connect(String url, Connection.Method method, Map<String,String> _header, Map<String,String> _cookie) throws IOException {
        return connect(url, method, _header, _cookie, emptyMap);
    }

    public static Connection.Response connect(String url, Connection.Method method, Map<String,String> _header, Map<String,String> _cookie, Map<String,String> _data) throws IOException {
        return connect(url, method, _header, _cookie, _data, false);
    }

    public static synchronized Connection.Response connect(String url, Connection.Method method, Map<String,String> _header, Map<String,String> _cookie, Map<String,String> _data, boolean ignoreType) throws IOException {
        return Jsoup.connect(url)
                .ignoreContentType(ignoreType)
                .headers(_header)
                .cookies(_cookie)
                .data(_data)
                .headers(baseHeaders)
                .method(method)
                .execute();
    }
}
package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.utils.NetLib;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import org.jsoup.Connection;

import java.io.IOException;

public class CookieInfo implements IInfo<CookieData> {
    private CookieData cache;

    @Override
    public ResultPack<CookieData> getInfo() {
        if (cache != null){
            return new ResultPack<>(true,cache,"");
        } else {
            CookieData data = new CookieData();
            boolean flag = true;
            String msg = "";
            try {
                Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd", Connection.Method.GET);
                data.cookie = response.cookie(data.getName());
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
                msg = e.getLocalizedMessage();
            }
            cache = data;
            return new ResultPack<>(flag,data,msg);
        }
    }

    /*
    private void testCookie(CookieData data){
        long distance = System.currentTimeMillis() - data.create;
        if(distance > 3 * 60 * 60 * 1000) data.outOfDate = true;
        else if (distance < 30 * 60 * 1000) data.outOfDate = false;
        else {
            Map<String,String> cookies = new HashMap<>();
            cookies.put("JSESSIONID",data.cookie);
            Connection.Response response;
            try {
                response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp", Connection.Method.GET,Collections.emptyMap(), cookies);
                data.outOfDate = response.statusCode() == 404;
            } catch (IOException e) {
                data.outOfDate = true;
            }
        }
    }

     */
}

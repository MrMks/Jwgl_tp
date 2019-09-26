package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.utils.NetLib;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import org.jsoup.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VerifyBitmapInfo implements IInfo<InputStream> {
    private IInfo<CookieData> info;
    public VerifyBitmapInfo(IInfo<CookieData> _info){
        info = _info;
    }

    @Override
    public ResultPack<InputStream> getInfo() {
        Map<String,String> header = new HashMap<>();
        header.put("Referer","http://jwgl.sdust.edu.cn/jsxsd/");

        Map<String,String> cookie = new HashMap<>();
        CookieData data = info.getInfo().getResult();
        cookie.put(data.getName(),data.cookie);

        InputStream stream = null;
        boolean flag = true;
        String msg = "";

        try {
            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/verifycode.servlet?t=" + Math.random(), Connection.Method.GET, header, cookie, Collections.emptyMap(),true);
            stream =  response.bodyStream();
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            msg = e.getLocalizedMessage();
        }
        return new ResultPack<>(flag,stream,msg);
    }
}

package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.data.LoginData;
import com.unknown.sdust.jwgl_tp.utils.EncodeInp;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;
import com.unknown.sdust.jwgl_tp.utils.NetLib;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivityInfo implements IInfo {
    private String encoded, code;
    private String account, password;
    private boolean rem_pass;
    private IInfo<CookieData> info;

    public LoginActivityInfo(String _account, String _password, boolean _rem_pass, String _code, IInfo<CookieData> _info){
        account = _account;
        password = _password;
        rem_pass = _rem_pass;
        encoded = EncodeInp.e(account) + "%%%" + EncodeInp.e(password);
        code = _code;
        info = _info;
    }

    @Override
    public ResultPack getInfo() {
        Map<String,String> data = new HashMap<>();
        data.put("encoded",encoded);
        data.put("RANDOMCODE",code);

        Map<String,String> cookie = new HashMap<>();
        CookieData cookieData = info.getInfo().getResult();
        cookie.put(cookieData.getName(),cookieData.cookie);

        Map<String,String> header = new HashMap<>();

        boolean flag = false;
        String result = "";
        String msg = "Unknown Error";
        try {
            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/xk/LoginToXk", Connection.Method.POST, header,cookie,data);

            if (!response.url().equals(new URL("http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp"))) {
                response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp", Connection.Method.GET, header, cookie);
            }

            Document document = response.parse();
            Element element = document.getElementById("Top1_divLoginName");
            if(element != null){
                result = element.childNode(0).toString().trim();
                flag = true;
            } else {
                msg = document.getElementsByClass("dlmi").get(0).childNode(13).childNode(0).toString().trim();
                flag = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag){
            LoginData loginData = Infos.loginDataInfo.getInfo().getResult();
            loginData.Account = account;
            loginData.Password = password;
            loginData.savePassword = rem_pass;

            loginData.Name = (result.contains("(") ? result.substring(0,result.indexOf("(")) : result);

            cookieData.logged = true;

            JsonRes.write(loginData, JsonRes.loginFile);
        }
        return new ResultPack<>(flag, null, msg);
    }
}

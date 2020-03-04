package com.unknown.sdust.jwgl_tp.data;

import android.util.Log;

import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.data.store.CalendarStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.data.store.TokenStore;
import com.unknown.sdust.jwgl_tp.utils.NetLib;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.unknown.sdust.jwgl_tp.Constant.TAG;

public class QzConnectImpl implements QzConnect {
    @Override
    public boolean isWebAccessible() {
        try {
            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd", Connection.Method.GET);
            return response.statusCode() == 200;
        } catch (IOException e){
            Log.w(TAG,e);
            return false;
        }
    }

    @Override
    public TokenStore getToken() {
        try {
            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd", Connection.Method.GET);
            String tk = response.cookie(TokenStore.NAME);
            if (tk == null) return null;
            else return new TokenStore(tk);
        } catch (IOException e) {
            Log.w(TAG,e);
            return null;
        }
    }

    @Override
    public boolean testToken(TokenStore token) {
        if(token == null || !token.selfCheck()) return false;
        else {
            Map<String,String> cookies = new HashMap<>();
            cookies.put(TokenStore.NAME,token.getToken());
            try {
                Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp", Connection.Method.GET, Collections.emptyMap(),cookies);
                return response.statusCode() == 200;
            } catch (IOException e){
                Log.w(TAG,e);
                return false;
            }
        }
    }

    @Override
    public boolean login(TokenStore token, AccountStore account, String code) {
        if (token == null || account == null || !token.selfCheck() || !account.selfCheck() || code == null || code.isEmpty()) return false;
        try {
            Map<String,String> data = new HashMap<>();
            data.put("encoded",account.getEncoded());
            data.put("RANDOMCODE",code);

            Map<String,String> cookie = new HashMap<>();
            cookie.put(TokenStore.NAME,token.getToken());

            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/xk/LoginToXk", Connection.Method.POST,Collections.emptyMap(),cookie,data);
            account.setFromLocal(true);
            return response.url().equals(new URL("http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp"));
        }catch (IOException e){
            Log.w(TAG,e);
            return false;
        }
    }

    @Override
    public CalendarStore getCalendar(TokenStore token) {
        if (token == null || !token.selfCheck()) return null;
        try {
            Map<String,String> cookie = new HashMap<>();
            cookie.put(TokenStore.NAME,token.getToken());
            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/jxzl/jxzl_query?Ves632DSdyV=NEW_XSD_WDZM", Connection.Method.GET,Collections.emptyMap(),cookie);
            if(response.statusCode() == 200){
                Document document = response.parse();
                String strDate = document.getElementById("kbtable").getElementsByTag("td").get(1).attr("title");
                strDate = strDate.replace("年","月");
                String[] strs = strDate.split("月");
                return new CalendarStore(Integer.decode(strs[0]),Integer.decode(strs[1]),Integer.decode(strs[2]));
            } else{
                return null;
            }
        } catch (IOException e){
            Log.w(TAG,e);
            return null;
        }
    }

    @Override
    public TableStore getTable(TokenStore token, String option) {
        if  (token == null || !token.selfCheck()) return null;
        try {
            Map<String,String> cookie = new HashMap<>();
            cookie.put(TokenStore.NAME,token.getToken());

            Map<String,String> data = new HashMap<>();
            if (option != null && !option.isEmpty()){
                data.put("xnxq01id",option);
            }

            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/xskb/xskb_list.do", Connection.Method.GET,Collections.emptyMap(),cookie,data);
            if (response.statusCode() == 200){
                TableStore store = new TableStore(option);
                Document document = response.parse();
                List<Element> trs = document.getElementById("kbtable").getElementsByTag("tr");
                trs.remove(0);
                store.addExtra(trs.remove(trs.size() - 1).text().trim());

                int time = 1;
                for(Element tr : trs){
                    Elements tds = tr.getElementsByTag("td");
                    int day = 1;
                    for (Element td : tds){
                        Element kbcontent = td.getElementsByClass("kbcontent").first();
                        List<TextNode> texts = kbcontent.textNodes();
                        Elements fonts = kbcontent.getElementsByTag("font");

                        for(int c_index = 0;c_index < fonts.size() / 3;c_index ++){
                            String name = texts.get(c_index * 2).text() + texts.get(c_index * 2 + 1).text();
                            String teacher = fonts.remove(0).text();
                            String strWeek = fonts.remove(0).text();
                            String room = fonts.remove(0).text();

                            store.addClass(new String[]{name,teacher,room,strWeek,""+day+" "+time});
                        }
                        day++;
                    }
                    time ++;

                }
                store.setOption(option);
                return store;
            } else {
                return null;
            }
        } catch (IOException e){
            Log.w(TAG,e);
            return null;
        }
    }

    @Override
    public boolean logout(TokenStore token) {
        if (token == null || !token.selfCheck()) return false;
        try {
            Map<String,String> cookie = new HashMap<>();
            cookie.put(TokenStore.NAME,token.getToken());

            Connection.Response response = NetLib.connect(
                    "http://jwgl.sdust.edu.cn/jsxsd/xk/LoginToXk?method=exit&tktime=" + System.currentTimeMillis(),
                    Connection.Method.GET,Collections.emptyMap(),cookie);
            return response.statusCode() == 200;
        } catch (IOException e){
            Log.w(TAG,e);
            return false;
        }
    }

    @Override
    public String getAccountName(TokenStore token) {
        return getAccount(token).getName();
    }

    @Override
    public AccountStore getAccount(TokenStore token) {
        if (token == null || !token.selfCheck()) return null;
        try {
            Map<String,String> cookie = new HashMap<>();
            cookie.put(TokenStore.NAME,token.getToken());

            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp", Connection.Method.GET,Collections.emptyMap(),cookie);
            if (response.statusCode() == 200){
                String str = response.parse().getElementById("Top1_divLoginName").text();
                String[] strings = str.split("\\(");
                AccountStore store = new AccountStore(strings[1].replace("\\)",""),"",false);
                store.setName(strings[0]);
                return store;
            } else {
                return null;
            }
        } catch (IOException e){
            Log.w(TAG,e);
            return null;
        }
    }

    @Override
    public InputStream getNewCodeImg(TokenStore token) {
        if (token == null || !token.selfCheck()) return null;
        try {
            Map<String,String> cookie = new HashMap<>();
            cookie.put(TokenStore.NAME,token.getToken());

            Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/verifycode.servlet" + "?t=" + Math.random(), Connection.Method.GET,
                    Collections.emptyMap(),cookie,Collections.emptyMap(),true);
            return response.bodyStream();
        } catch (IOException e){
            Log.w(TAG,e);
            return null;
        }
    }
}

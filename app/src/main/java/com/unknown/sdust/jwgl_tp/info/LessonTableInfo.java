package com.unknown.sdust.jwgl_tp.info;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.ILesson;
import com.unknown.sdust.jwgl_tp.ILessonTable;
import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.data.timeTable.Lesson;
import com.unknown.sdust.jwgl_tp.data.timeTable.LessonTable;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;
import com.unknown.sdust.jwgl_tp.utils.NetLib;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LessonTableInfo implements IInfo<ILessonTable> {
    private IInfo<CookieData> info;
    private ILessonTable cache;
    public LessonTableInfo(IInfo<CookieData> _info){
        info = _info;
    }

    @Override
    public ResultPack<ILessonTable> getInfo() {
        if(cache != null) return new ResultPack<>(true,cache,"");

        ILessonTable result = null;
        File file = new File(JsonRes.getPath(),getChild());
        if(file.exists()){
            Gson gson = new GsonBuilder().registerTypeAdapter(ILesson.class, (JsonDeserializer<Lesson>) (json, typeOfT, context) -> context.deserialize(json,Lesson.class)).create();
            try {
                FileReader fileReader = new FileReader(file);
                result =  gson.fromJson(fileReader,getKlass());
                fileReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cache = result;
        if(cache != null) return new ResultPack<>(true,cache,"");

        try {
            CookieData data = info.getInfo().getResult();
            if (!data.logged) return new ResultPack<>(false,null,"");

            cache = getTimeTable(data);
            cache.setFirstDay(getFirstDay());
            JsonRes.write(cache, JsonRes.tableFile);
            return new ResultPack<>(true,cache,"");
        } catch (Exception e) {
            e.printStackTrace();
            cache = null;
            return new ResultPack<>(false,null,e.getLocalizedMessage());
        }
    }

    private LessonTable getTimeTable(CookieData cookieData) throws IOException {
        LessonTable table = new LessonTable();

        Map<String,String> header = new HashMap<>();
        header.put("Referer","http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp");

        Map<String,String> cookie = new HashMap<>();
        cookie.put(cookieData.getName(),cookieData.cookie);

        Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/xskb/xskb_list.do?Ves632DSdyV=NEW_XSD_PYGL", Connection.Method.GET, header, cookie);
        Document document = response.parse();
        Element kbtable = document.getElementById("kbtable");
        Elements trs = kbtable.getElementsByTag("tr");

        for(int time = 1; time < trs.size() - 1;time++){
            Element tr = trs.get(time);
            Elements tds = tr.getElementsByTag("td");
            for(int day = 1; day <= tds.size();day++){
                Element kb = tds.get(day - 1).getElementsByClass("kbcontent").first();
                String[] strList = kb.text().split(" ");
                int index = -1;

                while (strList.length >= 5 && index + 1 < strList.length){
                    String name = strList[index += 1] + strList[index += 1];
                    String teacher = strList[index += 1];
                    String strWeek = strList[index += 1];
                    String room = strList[index += 1];
                    index += 1;

                    if(room.startsWith("JC") || room.startsWith("JB") || room.startsWith("JA") || room.startsWith("J网")) room = room.substring(1);
                    if(room.startsWith("Js")) room = room.replaceFirst("Js","S");

                    Matcher matcher = Pattern.compile("(\\d{1,2})(?:-(\\d{1,2}))?").matcher(strWeek);
                    while(matcher.find()){
                        int i,j;
                        for(i = j = Integer.decode(matcher.group(1));i <= (matcher.group(2) == null ? j :Integer.decode(matcher.group(2)));i++) {
                            if (strWeek.contains("单周")) {
                                if (i % 2 != 1) continue;
                            } else if (strWeek.contains("双周")) {
                                if (i % 2 != 0) continue;
                            }
                            table.addLesson(i,day,time,name,teacher,room);
                        }
                    }
                }
            }
        }
        for(String node : trs.get(6).getElementsByTag("td").eachText()){
            table.addExtra(node);
        }
        return table;
    }

    private Date getFirstDay() throws IOException, ParseException {
        Map<String,String> header = new HashMap<>();
        header.put("Referer","http://jwgl.sdust.edu.cn/jsxsd/framework/xsMain.jsp");
        Map<String,String> cookie = new HashMap<>();
        cookie.put("JSESSIONID",info.getInfo().getResult().cookie);
        Connection.Response response = NetLib.connect("http://jwgl.sdust.edu.cn/jsxsd/jxzl/jxzl_query?Ves632DSdyV=NEW_XSD_WDZM",Connection.Method.GET, header, cookie);

        Document document = response.parse();
        Element firstE = document.getElementsByTag("td").get(2);
        String day = firstE.attr("title");
        return new SimpleDateFormat("yyyy年MM月dd", Locale.CHINA).parse(day);
    }

    private String getChild() {
        return JsonRes.tableFile;
    }

    private Class<? extends ILessonTable> getKlass() {
        return LessonTable.class;
    }
}

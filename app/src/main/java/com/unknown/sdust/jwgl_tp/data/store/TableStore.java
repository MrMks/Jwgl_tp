package com.unknown.sdust.jwgl_tp.data.store;

import com.google.gson.annotations.Expose;
import com.unknown.sdust.jwgl_tp.data.IFileStore;
import com.unknown.sdust.jwgl_tp.data.ISelfCheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableStore implements IFileStore<TableStore>,ISelfCheck {

    @Expose private ArrayList<String[]> lessons;
    @Expose private String ext;
    private String option;
    private HashMap<Integer,ArrayList<LessonDetail>> list = new HashMap<>(30);

    public TableStore(String option){
        this.option = option;
    }

    public String getOption(){
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    //details[name,teacher,room,weeks,day&time]
    public void addClass(String[] details){
        if (lessons == null) lessons = new ArrayList<>();
        lessons.add(details);
        loadToList(details);
    }

    public List<LessonDetail> getLessons(int week){
        if (selfCheck()) return list.get(week);
        else return Collections.emptyList();
    }

    public void addExtra(String str){
        ext = str;
    }

    public String getExtra(){
        return ext;
    }

    private void loadToList(String[] l){
        if (l.length < 5) return;
        LessonDetail d = new LessonDetail(l[0],l[1],l[2],Integer.decode(l[4].replace(" ","")));

        Pattern pattern = Pattern.compile("(\\d{1,2})(?:-(\\d{1,2}))?");
        Matcher matcher = pattern.matcher(l[3]);

        while(matcher.find()){
            int i = Integer.decode(Objects.requireNonNull(matcher.group(1)));
            if (matcher.group(2) != null){
                int i_a = 2;
                int i_max = Integer.decode(Objects.requireNonNull(matcher.group(2)));
                if (l[3].contains("单周") && i % 2 == 0) i += 1;
                else if (l[3].contains("双周") && i % 2 == 1) i += 1;
                else i_a = 1;

                for(;i<=i_max;i+=i_a) {
                    if (list.get(i) == null) {
                        list.put(i,new ArrayList<>(30));
                    }
                    Objects.requireNonNull(list.get(i)).add(d);
                }
            } else {
                if (list.get(i) == null) {
                    list.put(i,new ArrayList<>(30));
                }
                Objects.requireNonNull(list.get(i)).add(d);
            }
        }
    }

    @Override
    public boolean selfCheck() {
        if (lessons == null) return false;
        if (list == null || list.isEmpty()) {
            if (list == null) list = new HashMap<>(30);
            for (String[] l : lessons) loadToList(l);
        }
        return true;
    }

    @Override
    public TableStore toFile() {
        return this;
    }

    public int getWeekSize() {
        return list.size();
    }

    public static class LessonDetail{
        @Expose private int daytime;
        @Expose private String name;
        @Expose private String teacher;
        @Expose private String room;

        LessonDetail(String name, String teacher, String room, int daytime){
            this.name = name;
            this.teacher = teacher;
            this.room = room;
            this.daytime = daytime;
        }

        public int getDaytime() {
            return daytime;
        }

        public String getName() {
            return name;
        }

        public String getTeacher() {
            return teacher;
        }

        public String getRoom() {
            return room;
        }
    }
}

package com.unknown.sdust.jwgl_tp.data.timeTable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Lesson {

    private static HashMap<String, Lesson> lessonHashMap = new HashMap<>();
    static Lesson getOrCreate(String n, String t){
        Lesson l;
        String k = (n + t).trim();
        if (!lessonHashMap.containsKey(k)) {
            l = new Lesson(n, t);
            lessonHashMap.put(k, l);
        } else {
            l = lessonHashMap.get(k);
        }
        return l;
    }

    private String name;
    private String teacher;
    private HashMap<Integer, HashMap<Integer, String>> roomMap = new HashMap<>();

    public Lesson(){}

    private Lesson(String n, String t){
        name = n;
        teacher = t;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher(){
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom(int week, int day, int time){
        return getRoom(week,day * 100 + time);
    }

    public String getRoom(int week, int day_time){
        if (roomMap.get(week) != null) return roomMap.get(week).get(day_time);
        return "";
    }

    public Set<Integer> keySet(int week){
        if (roomMap.get(week) != null) {
            return roomMap.get(week).keySet();
        }
        else return Collections.emptySet();
    }

    public void addRoom(int week, int day, int time, String room){
        if (!roomMap.containsKey(week)) roomMap.put(week,new HashMap<>());
        roomMap.get(week).put(day * 100 + time, room);
    }

    public HashMap<Integer, HashMap<Integer, String>> getRoomMap() {
        return roomMap;
    }

    public void setRoomMap(HashMap<Integer, HashMap<Integer, String>> roomMap) {
        this.roomMap = roomMap;
    }
}

package com.unknown.sdust.jwgl_tp.data.timeTable;

import android.util.SparseArray;

import com.unknown.sdust.jwgl_tp.ILesson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Lesson_v2 implements ILesson {

    private static HashMap<String, Lesson_v2> lessonHashMap = new HashMap<>();
    static Lesson_v2 getOrCreate(String n, String t){
        Lesson_v2 l;
        String k = (n + t).trim();
        if (!lessonHashMap.containsKey(k)) {
            l = new Lesson_v2(n, t);
            lessonHashMap.put(k, l);
        } else {
            l = lessonHashMap.get(k);
        }
        return l;
    }

    private String name;
    private String teacher;
    private SparseArray<HashMap<String, ArrayList<Integer>>> rooms = new SparseArray<>(25);

    public Lesson_v2(){}

    private Lesson_v2(String n, String t){
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
        return getRoom(week,day << 3 & time);
    }

    public String getRoom(int week, int day_time){
        if (rooms.get(week) == null) return "";
        for (String room : rooms.get(week).keySet()){
            if (rooms.get(week).get(room) == null) continue;
            for (int d_t : Objects.requireNonNull(rooms.get(week).get(room))){
                if (d_t == day_time){
                    return room;
                }
            }
        }
        return "";
    }

    public Set<Integer> keySet(int week){
        return null;
    }

    public void addRoom(int week, int day, int time, String room){
        if (rooms.get(week) == null) rooms.put(week,new HashMap<>());
        if (!rooms.get(week).containsKey(room)) rooms.get(week).put(room,new ArrayList<>(4));
        Objects.requireNonNull(rooms.get(week).get(room)).add(day * 100 + time);
    }
    public SparseArray<HashMap<String, ArrayList<Integer>>> getRooms() {
        return rooms;
    }

    public void setRooms(SparseArray<HashMap<String, ArrayList<Integer>>> rooms) {
        this.rooms = rooms;
    }
}

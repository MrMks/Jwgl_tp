package com.unknown.sdust.jwgl_tp;

import java.util.Set;

public interface ILesson {
    String getName();
    void setName(String name);
    String getTeacher();
    void setTeacher(String teacher);
    void addRoom(int week, int day, int time, String room);
    String getRoom(int week, int day, int time);
    String getRoom(int week, int day_time);
    Set<Integer> keySet(int week);
}

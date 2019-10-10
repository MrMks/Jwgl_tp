package com.unknown.sdust.jwgl_tp;

import java.util.Collection;
import java.util.Date;

public interface ILessonTable {
    void addLesson(int week, int day, int time, String name, String teacher, String room);
    Collection<ILesson> getLessons(int week);
    int getWeekCount();

    void addExtra(String ext);
    Collection<String> getExtras();

    void setFirstDay(Date date);
    Date getFirstDay();
}

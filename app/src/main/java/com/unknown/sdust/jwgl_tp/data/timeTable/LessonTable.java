package com.unknown.sdust.jwgl_tp.data.timeTable;

import com.unknown.sdust.jwgl_tp.ILesson;
import com.unknown.sdust.jwgl_tp.ILessonTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class LessonTable implements ILessonTable {
    private ArrayList<ILesson> lessons = new ArrayList<>(30);
    private int weekCount = 0;
    private ArrayList<String> extras = new ArrayList<>(4);
    private Date firstDay;

    public void addLesson(int iWeek, int iDay, int iTime, String name, String teacher, String room){
        weekCount = Math.max(weekCount,iWeek);
        Lesson lesson = Lesson.getOrCreate(name,teacher);
        if (!lessons.contains(lesson)) lessons.add(lesson);
        lesson.addRoom(iWeek,iDay,iTime,room);
    }

    @Override
    public Collection<ILesson> getLessons(int week) {
        if (weekCount < week) return null;
        ArrayList<ILesson> lessons = new ArrayList<>(15);
        for (ILesson lesson : this.lessons){
            Set<Integer> set = lesson.keySet(week);
            if (set != null && !set.isEmpty()) lessons.add(lesson);
        }
        return lessons;
    }

    @Override
    public int getWeekCount() {
        return weekCount;
    }

    public void addExtra(String extra) {
        extras.add(extra);
    }

    public ArrayList<String> getExtras() {
        return extras;
    }

    //use for GSON reflect
    public void setExtras(ArrayList<String> extras) {
        this.extras = extras;
    }

    public void setFirstDay(Date day){
        firstDay = day;
    }

    public Date getFirstDay(){
        return firstDay;
    }

}

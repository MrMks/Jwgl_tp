package com.unknown.sdust.jwgl_tp.data.timeTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class LessonTable {
    private ArrayList<Week> weeks = new ArrayList<>(26);
    private ArrayList<String> extras = new ArrayList<>(10);
    private Date firstDay;

    public void addLesson(ArrayList<Integer> iWeeks, int iDay, int iTime, String name, String teacher, String room) {
        for(int i : iWeeks) addLesson(i, iDay, iTime, name, teacher, room);
    }

    public void addLesson(int iWeek, int iDay, int iTime, String name, String teacher, String room){
        while (weeks.size() < iWeek) weeks.add(new Week());
        Week w = weeks.get(iWeek - 1);
        w.addLesson(iWeek,iDay,iTime,name,teacher,room);
    }

    public Week getWeek(int iWeek) {
        if (weeks.size() < iWeek) return null;
        return weeks.get(iWeek - 1);
    }

    public ArrayList<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(ArrayList<Week> weeks) {
        this.weeks = weeks;
    }

    public void addExtra(String extra) {
        extras.add(extra);
    }

    public ArrayList<String> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<String> extras) {
        this.extras = extras;
    }

    public void setFirstDay(Date day){
        firstDay = day;
    }

    public Date getFirstDay(){
        return firstDay;
    }

    public int getSize(){
        return weeks.size();
    }

    public class Week {
        ArrayList<Lesson> lessons = new ArrayList<>(15);

        void addLesson(int week, int day, int time, String name, String teacher, String room){
            Lesson l = Lesson.getOrCreate(name,teacher);
            l.addRoom(week, day, time, room);
            if (!lessons.contains(l)) lessons.add(l);
        }

        public Collection<Lesson> asCollection(){
            return lessons;
        }
    }
}

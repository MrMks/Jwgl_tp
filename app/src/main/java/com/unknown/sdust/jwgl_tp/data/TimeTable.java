package com.unknown.sdust.jwgl_tp.data;

import java.util.ArrayList;
import java.util.Date;

public class TimeTable {

    public ArrayList<Week> table = new ArrayList<>();
    public ArrayList<String> extra = new ArrayList<>();

    public Date firstDay;

    private int max_week_index = -1;
    private int max_day_index = -1;
    private int max_time_index = -1;

    public void addSingleClass(ArrayList<Integer> iWeeks, int iDay, int iTime, String name, String teacher, String room){
        for(int iWeek : iWeeks){
            while(table.size() < iWeek) table.add(new Week());
            Week week = table.get(iWeek - 1);
            week.addSingleClass(iDay,new SingleClass(iDay,iTime,name,teacher,room));
        }
    }

    public SingleClass getSingleClass(int iWeek, int iDay, int iTime){
        if(getWeek(iWeek) != null & getWeek(iWeek).getDay(iDay) != null){
            return getWeek(iWeek).getDay(iDay).getSingle(iTime);
        } else {
            return null;
        }
    }

    public void addExtra(String extra){
        this.extra.add(extra);
    }

    private ArrayList<String> getExtra(){
        return this.extra;
    }

    public int getSize(){
        checkS();
        return max_week_index + 1;
    }
    public int getWeekSize(){
        checkS();
        return max_day_index + 1;
    }
    public int getDaySize(){
        checkS();
        return max_time_index + 1;
    }

    private void checkS(){
        if(max_week_index < 0 || max_time_index < 0 || max_day_index < 0){
            max_week_index = table.size() - 1;
            for(Week week : table){
                max_day_index = Math.max(week.week.size() - 1,max_day_index);
                for(Day day : week.week){
                    max_time_index = Math.max(max_time_index,day.day.size() - 1);
                }
            }
        }
    }

    private Week getWeek(int iWeek){
        return table.size() > iWeek - 1 ? table.get(iWeek - 1) : null;
    }

    public class Week{
        public ArrayList<Day> week = new ArrayList<>();
        void addSingleClass(int iDay, SingleClass singleClass){
            while(week.size() < iDay) week.add(new Day());
            Day day = week.get(iDay - 1);
            day.addSingleClass(singleClass);
        }
        Day getDay(int iDay){
            return week.size() > iDay - 1 ? week.get(iDay - 1) : null;
        }
    }

    public class Day{
        public ArrayList<SingleClass> day = new ArrayList<>();
        void addSingleClass(SingleClass singleClass){
            while(day.size() < singleClass.time) day.add(null);
            day.add(singleClass.time - 1,singleClass);
        }
        SingleClass getSingle(int iTime){
            return day.size() > iTime - 1 ? day.get(iTime - 1) : null;
        }
    }

    public class SingleClass {
        public int day;
        public int time;
        public String name;
        public String teacher;
        public String room;
        SingleClass(int day, int time, String className, String teacher, String classRoom){
            this.day = day;
            this.time = time;
            this.name = className;
            this.teacher = teacher;
            this.room = classRoom;
        }
    }
}

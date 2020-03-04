package com.unknown.sdust.jwgl_tp.data.store;

import com.google.gson.annotations.Expose;
import com.unknown.sdust.jwgl_tp.data.IFileStore;
import com.unknown.sdust.jwgl_tp.data.ISelfCheck;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarStore implements IFileStore<CalendarStore>, ISelfCheck {

    //the date of the first day of this term
    @Expose private Integer year,month,day;
    private ArrayList<Integer> list;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

    public CalendarStore(){}

    public CalendarStore(int y, int m, int d){
        year = y;
        month = m;
        day = d;
    }

    public int getWeekIndex(int year, int month, int day){
        if (list == null) initList();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.set(year, month - 1, day);
        c.add(Calendar.DATE,c.get(Calendar.DAY_OF_WEEK) - 1);
        return list.indexOf(c.get(Calendar.YEAR) * 1000 + c.get(Calendar.MONTH) * 100 + c.get(Calendar.DAY_OF_MONTH)) + 1;
    }

    public int getWeekIndex(){
        if (list == null) initList();
        Calendar c = calendar;
        c.add(Calendar.DATE,c.get(Calendar.DAY_OF_WEEK) - 1);
        return list.indexOf(c.get(Calendar.YEAR) * 1000 + c.get(Calendar.MONTH) * 100 + c.get(Calendar.DAY_OF_MONTH)) + 1;
    }

    private void initList(){
        list = new ArrayList<>(28);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.set(year,month - 1,day,0,0);
        while(list.size() < 28){
            list.add(c.get(Calendar.YEAR) * 1000 + c.get(Calendar.MONTH) * 100 + c.get(Calendar.DAY_OF_MONTH));
            c.add(Calendar.DATE,7);
        }
    }

    @Override
    public CalendarStore toFile() {
        return this;
    }

    @Override
    public boolean selfCheck() {
        return year != null && month != null && day != null
                && year > calendar.get(Calendar.YEAR) - 1 && month >= 0 && month < 12
                && day > 0 && day <= 31;
    }
}
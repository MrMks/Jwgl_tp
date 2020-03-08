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

    public CalendarStore(int y, int m, int d){
        year = y;
        month = m;
        day = d;
    }

    public int getWeekIndex(){
        if (list == null) initList();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int i = c.get(Calendar.DAY_OF_WEEK);
        if (i == Calendar.SUNDAY) i += 7;
        c.add(Calendar.DATE,2 - i);
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
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        return year != null && month != null && day != null
                && year > calendar.get(Calendar.YEAR) - 1 && month >= 0 && month < 12
                && day > 0 && day <= 31;
    }
}

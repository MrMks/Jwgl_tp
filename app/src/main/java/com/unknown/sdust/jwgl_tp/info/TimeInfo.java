package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeInfo implements IInfo<String> {
    @Override
    public ResultPack<String> getInfo() {
        String str = new SimpleDateFormat("yyyy-MM-dd 星期u", Locale.CHINA).format(new Date());
        String[] days = new String[]{"一","二","三","四","五","六","日"};
        str = str.substring(0,str.length() - 1) + days[Integer.parseInt(str.substring(str.length() - 1)) - 1];
        //Calendar calendar = Calendar.getInstance(Locale.CHINA);
        //calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //str =  str.concat(days[calendar.get(Calendar.DAY_OF_WEEK) - 2]);
        return new ResultPack<>(true,str,"");
    }
}

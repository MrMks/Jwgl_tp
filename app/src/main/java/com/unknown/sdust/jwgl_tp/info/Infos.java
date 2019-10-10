package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.ILessonTable;
import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.data.LoginData;
import com.unknown.sdust.jwgl_tp.data.TimeTable;
import com.unknown.sdust.jwgl_tp.data.VersionData;

public class Infos {
    public static IInfo<LoginData> loginDataInfo = new LoginDataInfo();
    public static IInfo<CookieData> cookieInfoRead = new CookieInfo();
    public static IInfo<String[]> baseInfoRead = new BaseInfo(loginDataInfo);
    public static IInfo<ILessonTable> lessonInfoRead = new LessonTableInfo(cookieInfoRead);
    public static IInfo<VersionData> versionInfoRead = new VersionInfo();

    //lasts
    public static IInfo<TimeTable> tableInfoRead = new TimeTableInfo(cookieInfoRead);
}

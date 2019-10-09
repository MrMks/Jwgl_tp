package com.unknown.sdust.jwgl_tp;

import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.data.LoginData;
import com.unknown.sdust.jwgl_tp.data.TimeTable;
import com.unknown.sdust.jwgl_tp.data.VersionData;
import com.unknown.sdust.jwgl_tp.data.timeTable.LessonTable;
import com.unknown.sdust.jwgl_tp.info.BaseInfo;
import com.unknown.sdust.jwgl_tp.info.CookieInfo;
import com.unknown.sdust.jwgl_tp.info.LessonTableInfo;
import com.unknown.sdust.jwgl_tp.info.LoginDataInfo;
import com.unknown.sdust.jwgl_tp.info.TimeTableInfo;
import com.unknown.sdust.jwgl_tp.info.VersionInfo;

public class Infos {
    public static IInfo<LoginData> loginDataInfo = new LoginDataInfo();
    public static IInfo<CookieData> cookieInfoRead = new CookieInfo();
    public static IInfo<TimeTable> tableInfoRead = new TimeTableInfo(cookieInfoRead);
    public static IInfo<String[]> baseInfoRead = new BaseInfo(loginDataInfo);
    public static IInfo<LessonTable> lessonInfoRead = new LessonTableInfo(cookieInfoRead);
    public static IInfo<VersionData> versionInfoRead = new VersionInfo();
}

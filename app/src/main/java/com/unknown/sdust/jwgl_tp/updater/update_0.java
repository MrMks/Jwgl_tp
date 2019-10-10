package com.unknown.sdust.jwgl_tp.updater;

import com.unknown.sdust.jwgl_tp.AbstractUpdater;
import com.unknown.sdust.jwgl_tp.data.TimeTable;
import com.unknown.sdust.jwgl_tp.data.timeTable.LessonTable;
import com.unknown.sdust.jwgl_tp.info.Infos;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

public class update_0 extends AbstractUpdater {
    @Override
    public void updateLogin() {

    }

    @Override
    public void updateTable() {
        ResultPack<TimeTable> pack = Infos.tableInfoRead.getInfo();
        if (!pack.isPresent()) return;

        TimeTable table = pack.getResult();
        LessonTable lessons = new LessonTable();

        lessons.setFirstDay(table.firstDay);

        for (int week = 1; week < table.getSize() + 1; week++){
            for (int day = 1; day < table.getWeekSize() + 1; day++){
                for (int time = 1; time < table.getDaySize() + 1; time++){
                    TimeTable.SingleClass single = table.getSingleClass(week,day,time);
                    if (single == null) continue;
                    lessons.addLesson(week,day,time,single.name,single.teacher,single.room);
                }
            }
        }

        JsonRes.write(lessons,JsonRes.tableFile);
    }
}

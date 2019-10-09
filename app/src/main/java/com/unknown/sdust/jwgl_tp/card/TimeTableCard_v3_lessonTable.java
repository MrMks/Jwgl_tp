package com.unknown.sdust.jwgl_tp.card;

import android.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.timeTable.Lesson;
import com.unknown.sdust.jwgl_tp.data.timeTable.LessonTable;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TimeTableCard_v3_lessonTable extends CardContent<LessonTable> {
    public TimeTableCard_v3_lessonTable(AppCompatActivity activity, IInfo<LessonTable> info) {
        super(activity,info);
        init();
    }


    private LessonTable table;
    @Override
    public void run() {
        ResultPack<LessonTable> pack = getInfo();
        if (!pack.isPresent()){
            getHandler().post(()->{
                TextView view = findViewById(R.id.text_table_info);
                view.setText(R.string.load_failed);
                view.setOnClickListener(v -> new Thread(this).start());
            });
            return;
        } else {
            getHandler().post(()->{
                TextView view = findViewById(R.id.text_table_info);
                view.setOnClickListener(v -> {});
            });
        }

        table = pack.getResult();
        int week_now = getNowWeekIndex(table.getFirstDay());
        int week_max = table.getSize();

        setWeekInfo(week_now,week_max);
        setWeekSelector(week_now,week_max);
        setWeekTable(week_now);

        setClickListeners();
    }

    private void init(){
        getHandler().post(() -> {
            ((TextView)findViewById(R.id.text_table_info)).setText(R.string.loading);
            findViewById(R.id.switch_table_show).setClickable(false);
            findViewById(R.id.button_week_select).setVisibility(View.GONE);
            findViewById(R.id.divider).setVisibility(View.GONE);
            findViewById(R.id.week_select_parent).setVisibility(View.GONE);
            findViewById(R.id.week_select).setVisibility(View.VISIBLE);
            findViewById(R.id.divider2).setVisibility(View.GONE);
            findViewById(R.id.scroll).setVisibility(View.GONE);
            findViewById(R.id.table).setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected int getCardId() {
        return 0;
    }

    @Override
    protected int getTopToBottom() {
        return 0;
    }

    @Override
    protected int getTopToTop() {
        return 0;
    }

    private int getNowWeekIndex(Date first) {
        int days = (int)((new Date().getTime() - first.getTime())/(24 * 60 * 60 * 1000));
        return 1 + days / 7;
    }

    private void setWeekInfo(int now, int max){
        setWeekInfo(now + "/" + max);
    }

    private void setWeekInfo(CharSequence str){
        TextView view = findViewById(R.id.text_table_info);
        getHandler().post(()-> view.setText(str));
    }

    private void setWeekSelector(int now, int max){
        ViewGroup group = findViewById(R.id.week_select);
        for(int i = 1; i <= max; i++){
            TextView view = new TextView(getContext());
            view.setText(String.valueOf(i));
            view.setTextSize(20);
            view.setBackgroundColor(0x00);
            view.setPadding(dp2px(4),0,dp2px(4),0);
            if(i == now) view.setTextColor(0xFF00FF00);
            final int f_i = i;
            view.setOnClickListener((v)->{
                setWeekInfo(f_i,max);
                setWeekTable(f_i);
            });
            getHandler().post(()->group.addView(view));
        }
    }

    private String[] days = new String[]{"一","二","三","四","五","六","日"};

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 星期u", Locale.CHINA);
    private void setWeekTable(int select){
        TableLayout tableLayout = findViewById(R.id.table);
        getHandler().post(tableLayout::removeAllViews);

        HashSet<TextView> views = new HashSet<>();
        int w_max = 0;
        int h_max = 0;
        {
            Date first = table.getFirstDay();
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(first);
            calendar.add(Calendar.DATE,select * 7 - 8);

            TableRow row = new TableRow(getContext());
            for(int day = 1;day <= 7;day++){
                calendar.add(Calendar.DATE,1);
                TextView view = new TextView(getContext());
                String str = format.format(calendar.getTime());
                str = str.substring(0,str.length() - 1) + days[Integer.parseInt(str.substring(str.length() - 1)) - 1];
                view.setText(str);
                view.setPadding(dp2px(4),dp2px(4),dp2px(4),dp2px(4));
                views.add(view);
                row.addView(view);
            }
            getHandler().post(()->tableLayout.addView(row));
        }

        HashSet<TextView> t_views = new HashSet<>();
        {
            Collection<Lesson> lessons = table.getWeek(select).asCollection();
            TableRow[] rows = new TableRow[5];
            for(int i = 0;i < 5;i++) {
                rows[i] = new TableRow(getContext());
                if(i % 2 == 0) rows[i].setBackgroundColor(0x2F000000);
            }
            SparseArray<ArrayList<Lesson>> rec = new SparseArray<>();
            for(Lesson lesson: lessons){
                Set<Integer> keys = lesson.keySet(select);
                for (int day_time:keys){
                    int day = day_time / 100;
                    int time = day_time % 100;

                    TableRow row = rows[time - 1];
                    while (row.getChildCount() < day) {
                        TextView v = new TextView(getContext());
                        t_views.add(v);
                        row.addView(v);
                    }
                    TextView view = (TextView) row.getChildAt(day - 1);

                    String title,msg;
                    if (rec.get(day_time) == null){
                        rec.put(day_time, new ArrayList<>(5));
                        rec.get(day_time).add(lesson);

                        view.setText(lesson.getName());
                        view.append("\n");
                        view.append(lesson.getRoom(select,day_time));
                        title = lesson.getName();
                        msg = lesson.getTeacher() + "\n" + lesson.getRoom(select,day_time);
                    } else {
                        rec.get(day_time).add(lesson);
                        StringBuilder str = new StringBuilder();
                        for (Lesson l : rec.get(day_time)){
                            str.append(l.getName()).append("\n").append(l.getTeacher()).append("\n").append(l.getRoom(select,day_time));
                            str.append("\n--------\n");
                        }
                        view.setText(R.string.multi_classes);
                        title = getContext().getResources().getString(R.string.multi_classes);
                        msg = str.toString();
                    }
                    view.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                            .setTitle(title)
                            .setMessage(msg)
                            .setPositiveButton(R.string.ok, (dialog, which) -> {})
                            .show());
                    view.setPadding(dp2px(4),dp2px(4),dp2px(4),dp2px(4));
                    view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
                    w_max = Math.max(view.getMeasuredWidth(),w_max);
                    h_max = Math.max(view.getMeasuredHeight(),h_max);
                }
            }
            for (TableRow row:rows){
                if(row.getChildCount() == 0){
                    TextView v = new TextView(getContext());
                    t_views.add(v);
                    row.addView(v);
                }
                getHandler().post(()->tableLayout.addView(row));
            }
        }
            /*
            for(int time = 1;time < table.getDaySize(); time++){
                final TableRow row = new TableRow(getContext());
                for(int day = 1;day <= 7;day ++){
                    final Collection<Lesson> single = table.getWeek(select).asCollection();
                    TextView view = new TextView(getContext());
                    if (single != null){
                        view.setText(single.name);
                        view.append("\n");
                        view.append(single.room);
                        view.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                                .setTitle(single.name)
                                .setMessage(single.teacher + "\n" + single.room)
                                .setPositiveButton(R.string.ok, (dialog, which) -> {})
                                .show());
                        view.setPadding(dp2px(4),dp2px(4),dp2px(4),dp2px(4));
                        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
                        w_max = Math.max(view.getMeasuredWidth(),w_max);
                        h_max = Math.max(view.getMeasuredHeight(),h_max);
                    }
                    views.add(view);
                    row.addView(view);
                }
                if(time % 2 == 1) row.setBackgroundColor(0x2F000000);
                getHandler().post(() -> tableLayout.addView(row));


            }
             */

        for(TextView view : t_views){
            view.setHeight(h_max);
            view.setWidth(w_max);
        }

        final int f_h_max = h_max;
        final int f_w_max = w_max;
        for(TextView view : views){
            getHandler().post(()->{
                view.setHeight(f_h_max);
                view.setWidth(f_w_max);
            });
        }

    }
    private void setClickListeners() {
        getHandler().post(() -> {
            findViewById(R.id.switch_table_show).setOnClickListener(this::showTable);
            findViewById(R.id.button_week_select).setOnClickListener(this::showSelector);
        });
    }

    private boolean select_on = false;
    private void showTable(View v){
        v.setOnClickListener(this::hideTable);
        findViewById(R.id.button_week_select).setVisibility(View.VISIBLE);
        if(select_on) {
            findViewById(R.id.week_select_parent).setVisibility(View.VISIBLE);
            findViewById(R.id.divider).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.divider2).setVisibility(View.VISIBLE);
        findViewById(R.id.scroll).setVisibility(View.VISIBLE);
    }

    private void hideTable(View v){
        v.setOnClickListener(this::showTable);
        findViewById(R.id.button_week_select).setVisibility(View.GONE);
        findViewById(R.id.divider).setVisibility(View.GONE);
        findViewById(R.id.week_select_parent).setVisibility(View.GONE);
        findViewById(R.id.divider2).setVisibility(View.GONE);
        findViewById(R.id.scroll).setVisibility(View.GONE);
    }

    private void showSelector(View v){
        v.setOnClickListener(this::hideSelector);
        ((ImageButton)v).setImageResource(R.drawable.arrow_up_15);
        findViewById(R.id.divider).setVisibility(View.VISIBLE);
        findViewById(R.id.week_select_parent).setVisibility(View.VISIBLE);
        select_on = true;
    }

    private void hideSelector(View v){
        v.setOnClickListener(this::showSelector);
        ((ImageButton)v).setImageResource(R.drawable.arrow_down_15);
        findViewById(R.id.divider).setVisibility(View.GONE);
        findViewById(R.id.week_select_parent).setVisibility(View.GONE);
        select_on = false;
    }
}

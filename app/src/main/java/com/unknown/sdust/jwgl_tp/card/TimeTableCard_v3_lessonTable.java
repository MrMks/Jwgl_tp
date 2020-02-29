package com.unknown.sdust.jwgl_tp.card;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.ILesson;
import com.unknown.sdust.jwgl_tp.ILessonTable;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class TimeTableCard_v3_lessonTable extends CardContent<ILessonTable> {
    public TimeTableCard_v3_lessonTable(Activity activity, IInfo<ILessonTable> info) {
        super(activity,info);
        init();
    }

    private ILessonTable table;
    @Override
    public void run() {
        ResultPack<ILessonTable> pack = getInfo();
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
        int week_max = table.getWeekCount();

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
        LinearLayout linearLayout = findViewById(R.id.week_linear);
        TableLayout tableLayout = findViewById(R.id.table);
        getHandler().post(linearLayout::removeAllViews);
        getHandler().post(tableLayout::removeAllViews);

        TableRow[] rows = new TableRow[6];
        View extra;
        HashSet<TextView> views = new HashSet<>();
        int w_max = 0;
        int h_max = 0;
        //时间栏
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
            //getHandler().post(()->tableLayout.addView(row));
            rows[0] = row;
        }
        //课程表
        {
            Collection<ILesson> lessons = table.getLessons(select);
            for(int i = 1;i < 6;i++) {
                rows[i] = new TableRow(getContext());
                if(i % 2 == 1) rows[i].setBackgroundColor(0x2F000000);
            }
            SparseArray<ArrayList<ILesson>> rec = new SparseArray<>();
            for(ILesson lesson: lessons){
                for (int day_time:lesson.keySet(select)){
                    int day = day_time >> 3;
                    int time = day_time & 7;

                    TableRow row = rows[time];
                    while (row.getChildCount() < day) {
                        TextView v = new TextView(getContext());
                        if (row.getChildCount() == 0) views.add(v);
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
                        for (ILesson l : rec.get(day_time)){
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
        }
        // 备注栏
        {
            TextView text = new TextView(getContext());
            text.setText("");
            for (String ext : table.getExtras()){
                text.append(ext);
                text.append("\n");
            }
            text.setPadding(dp2px(4),dp2px(4),dp2px(4),dp2px(4));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = dp2px(8);
            params.rightMargin = dp2px(8);
            params.bottomMargin = dp2px(8);
            text.setLayoutParams(params);
            extra = text;
        }

        for (TableRow row:rows){
            if(row.getChildCount() == 0){
                TextView v = new TextView(getContext());
                views.add(v);
                row.addView(v);
            }
            getHandler().post(()->tableLayout.addView(row));
        }
        for(TextView view : views){
            view.setHeight(h_max);
            view.setWidth(w_max);
        }
        getHandler().post(()->{
            linearLayout.addView(tableLayout);
            linearLayout.addView(extra);
        });

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

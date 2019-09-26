package com.unknown.sdust.jwgl_tp.card;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.TimeTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimeTableCard extends CardContent<TimeTable> {

    private int load_stage = 0;

    public TimeTableCard(AppCompatActivity activity, IInfo<TimeTable> info){
        super(activity,info);
        init();
    }

    protected void init(){
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
    public void run() {
        TimeTable timeTable = getInfo().getResult();
        try {
            switch (load_stage){
                default:
                case 0 :
                    int[] w_d = getWeekWithDay(timeTable.firstDay);
                    week_now = w_d[0];
                    dayInWeek_now = w_d[1];
                    week_selected = week_now;
                    load_stage = 1;
                case 1:
                    week_max = timeTable.getSize();
                    load_stage = 2;
                case 2:
                    setWeekSelector();
                    getHandler().post(() -> {
                        findViewById(R.id.switch_table_show).setOnClickListener(this::showTable);
                        findViewById(R.id.button_week_select).setOnClickListener(this::showSelector);
                    });
                    load_stage = 3;
                case 3:
                    setWeekInfo(week_selected);
                    setTimeTable(week_selected);
                    getHandler().post(() -> findViewById(R.id.switch_table_show).setClickable(true));
            }
        } catch (final Exception e) {
            e.printStackTrace();
            getHandler().post(() -> {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                TextView view = findViewById(R.id.text_table_info);
                view.setText(R.string.load_failed);
                view.setOnClickListener(v -> new Thread(this).start());
            });
        }
    }

    private int week_now, week_max, week_selected;
    private int dayInWeek_now;

    private void setWeekInfo(int week){
        String str = week + "/" + week_max;
        getHandler().post(() -> ((TextView)findViewById(R.id.text_table_info)).setText(str));
    }

    private void setWeekSelector(){
        final LinearLayout linearLayout = findViewById(R.id.week_select);
        getHandler().post(linearLayout::removeAllViews);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16,4,16,4);

        for(int i = 1;i <= week_max;i++){
            final TextView view = new TextView(getContext());
            view.setLayoutParams(params);
            view.setText(String.valueOf(i));
            view.setTextSize(20);
            view.setBackgroundColor(0x00);
            if(i == week_now) view.setTextColor(0xFF00FF00);
            view.setOnClickListener(v -> {
                week_selected = Integer.parseInt((String)((TextView)v).getText());
                run();
            });
            getHandler().post(() -> linearLayout.addView(view));
        }
    }

    private void setTimeTable(int week) {
        final TableLayout tableLayout = findViewById(R.id.table);
        getHandler().post(tableLayout::removeAllViews);

        int w_max = 0;
        int h_max = 0;

        ArrayList<TextView> views = new ArrayList<>();
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16,2,16,2);

        TimeTable timeTable = getInfo().getResult();

        {
            final TableRow row = new TableRow(getContext());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 星期u",Locale.CHINA);
            String[] days = new String[]{"一","二","三","四","五","六","日"};
            long first_day_in_week = System.currentTimeMillis() / (24 * 60 * 60 * 1000) - (week_now - week) * 7 - dayInWeek_now;
            long remain = System.currentTimeMillis() % (24 * 60 * 60 * 1000);
            for(int day = 1;day <= 7;day++){
                TextView view = new TextView(getContext());
                String str = format.format((first_day_in_week + day) * (24 * 60 * 60 * 1000) + remain);
                str = str.substring(0,str.length() - 1) + days[Integer.parseInt(str.substring(str.length() - 1)) - 1];
                view.setText(str);
                view.setLayoutParams(params);
                view.setPadding(4,2,4,2);
                views.add(view);
                row.addView(view);
            }
            getHandler().post(() -> tableLayout.addView(row,0));
        }

        for(int time = 1;time < timeTable.getDaySize(); time++){
            final TableRow row = new TableRow(getContext());
            for(int day = 1;day <= 7;day ++){
                final TimeTable.SingleClass single = timeTable.getSingleClass(week,day,time);
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
                    view.setLayoutParams(params);
                    view.setPadding(4,2,4,2);
                    view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
                    w_max = Math.max(view.getMeasuredWidth(),w_max);
                    h_max = Math.max(view.getMeasuredHeight(),h_max);
                }
                views.add(view);
                row.addView(view,params);
            }
            row.setPadding(2,2,2,2);
            if(time % 2 == 1) row.setBackgroundColor(0x2F000000);
            final int finalTime = time;
            getHandler().post(() -> tableLayout.addView(row, finalTime));
        }

        for(final TextView view : views){
            final int finalW_max = w_max;
            final int finalH_max = h_max;
            getHandler().post(() -> {
                view.setWidth(finalW_max);
                view.setHeight(finalH_max);
            });
        }

        System.gc();
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

    private int[] getWeekWithDay(Date first) {
        int days = (int)((new Date().getTime() - first.getTime())/(24 * 60 * 60 * 1000));
        int[] data = new int[2];
        data[0] = 1 + days / 7;
        data[1] = 1 + days % 7;

        return data;
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
}

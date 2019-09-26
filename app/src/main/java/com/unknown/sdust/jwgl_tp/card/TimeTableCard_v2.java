package com.unknown.sdust.jwgl_tp.card;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.TimeTable;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class TimeTableCard_v2 extends TimeTableCard {
    public TimeTableCard_v2(AppCompatActivity activity, IInfo<TimeTable> info) {
        super(activity, info);
        super.init();
    }

    @Override
    public void run() {
        //super.run();
        run_v2();
    }

    private TimeTable table;
    private void run_v2(){
        ResultPack<TimeTable> pack = getInfo();
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
        int week_now = getNowWeekIndex(table.firstDay);
        int week_max = table.getSize();

        setWeekInfo(week_now,week_max);
        setWeekSelector(week_now,week_max);
        setWeekTable(week_now);

        setClickListeners();
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

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 星期u",Locale.CHINA);
    private void setWeekTable(int select){
        TableLayout tableLayout = findViewById(R.id.table);
        getHandler().post(tableLayout::removeAllViews);

        HashSet<TextView> views = new HashSet<>();
        int w_max = 0;
        int h_max = 0;
        {
            Date first = table.firstDay;
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

        {
            for(int time = 1;time < table.getDaySize(); time++){
                final TableRow row = new TableRow(getContext());
                for(int day = 1;day <= 7;day ++){
                    final TimeTable.SingleClass single = table.getSingleClass(select,day,time);
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

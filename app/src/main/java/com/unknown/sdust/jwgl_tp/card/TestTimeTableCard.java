package com.unknown.sdust.jwgl_tp.card;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.TimeTable;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class TestTimeTableCard extends CardContent<TimeTable> {
    public TestTimeTableCard(AppCompatActivity activity, IInfo<TimeTable> _info) {
        super(activity, _info);
    }

    private TimeTable timeTable;
    private CardView card;
    @Override
    public void run() {
        initCard();
        ResultPack<TimeTable> pack = getInfo();
        if(!pack.isPresent()) {
            TextView view = findViewById(R.id.testTimeTable_timeInfo);
            setWeekInfo(getContext().getText(R.string.load_failed));
            view.setOnClickListener((v)->run());
            return;
        } else {
            TextView view = card.findViewById(R.id.testTimeTable_timeInfo);
            view.setOnClickListener((v)->{});
        }

        timeTable = getInfo().getResult();
        int week_now = getNowWeekIndex(timeTable.firstDay);
        int week_max = timeTable.getSize();

        setWeekInfo(week_now,week_max);
        setWeekSelector(week_now,week_max);
        setWeekTable(week_now);

        setClickListeners();
        //ConstraintLayout layout = findViewById(R.id.constraintLayout);
        //getHandler().post(()->layout.addView(card));
    }

    private void initCard(){
        card = generate(getContext());
        ConstraintLayout layout = new ConstraintLayout(getContext());
        layout.setId(R.id.testTimeTableLayout);
        card.addView(layout);

        //时间信息
        {
            TextView view = new TextView(getContext());
            view.setText(R.string.loading);
            view.setId(R.id.testTimeTable_timeInfo);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.verticalBias = 0.0f;
            params.setMargins(dp2px(8),dp2px(8),dp2px(8),dp2px(8));
            params.topToTop = R.id.testTimeTableLayout;
            params.bottomToBottom = R.id.testTimeTableLayout;
            params.startToStart = R.id.testTimeTableLayout;

            layout.addView(view,params);
        }

        //周选择器开关
        {
            ImageButton view = new ImageButton(getContext());
            view.setId(R.id.testTimeTable_imageButton);
            view.setImageResource(R.drawable.arrow_down_15);
            view.setScaleX(0.6f);
            view.setScaleY(0.6f);
            //view.setPadding(dp2px(4),dp2px(4),dp2px(4),dp2px(4));
            view.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            params.startToEnd = R.id.testTimeTable_timeInfo;
            params.topToTop = R.id.testTimeTable_timeInfo;
            params.bottomToBottom = R.id.testTimeTable_timeInfo;

            layout.addView(view,params);
        }

        //详细课表开关
        {
            Switch view = new Switch(getContext());
            view.setId(R.id.testTimeTable_switch);
            view.setClickable(false);
            view.setChecked(false);
            view.setScaleX(0.7f);
            view.setScaleY(0.7f);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topToTop = R.id.testTimeTable_timeInfo;
            params.bottomToBottom = R.id.testTimeTable_timeInfo;
            params.endToEnd = R.id.testTimeTableLayout;
            params.rightMargin = dp2px(8);

            layout.addView(view,params);
        }

        //分隔栏
        {
            View view = new View(getContext());
            view.setBackgroundColor(getContext().getResources().getColor(R.color.dividerDark));
            view.setId(R.id.testTimeTable_divider1);
            view.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp2px(6),dp2px(8),dp2px(6),0);
            params.height = dp2px(1);
            params.startToStart = R.id.testTimeTableLayout;
            params.endToEnd = R.id.testTimeTableLayout;
            params.topToBottom = R.id.testTimeTable_timeInfo;

            layout.addView(view,params);
        }

        //周选择器
        {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setId(R.id.testTimeTable_weekSelector);

            HorizontalScrollView view = new HorizontalScrollView(getContext());
            view.addView(linearLayout);
            view.setId(R.id.testTimeTable_weekSelectorScroll);
            view.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp2px(8),dp2px(8),dp2px(8),0);
            params.topToBottom = R.id.testTimeTable_divider1;
            params.startToStart = R.id.testTimeTableLayout;
            params.endToEnd = R.id.testTimeTableLayout;

            layout.addView(view,params);
        }

        //分隔栏
        {
            View view = new View(getContext());
            view.setBackgroundColor(getContext().getResources().getColor(R.color.dividerDark));
            view.setId(R.id.testTimeTable_divider2);
            view.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp2px(6),dp2px(8),dp2px(6),0);
            params.height = dp2px(1);
            params.startToStart = R.id.testTimeTableLayout;
            params.endToEnd = R.id.testTimeTableLayout;
            params.topToBottom = R.id.testTimeTable_weekSelector;

            layout.addView(view,params);
        }

        //周课程表容器
        {
            TableLayout view = new TableLayout(getContext());
            view.setId(R.id.testTimeTable_tableLayout);
            //view.setOrientation(LinearLayout.HORIZONTAL);
            view.setHorizontalScrollBarEnabled(true);
            view.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp2px(8),dp2px(8),dp2px(8),0);
            params.startToStart = R.id.testTimeTableLayout;
            params.endToEnd = R.id.testTimeTableLayout;
            params.topToBottom = R.id.testTimeTable_divider2;
            params.bottomToBottom = R.id.testTimeTableLayout;

            layout.addView(view,params);
        }

        ConstraintLayout base = findViewById(R.id.constraintLayout);
        getHandler().post(()->base.addView(card));
    }

    private int getNowWeekIndex(Date first) {
        int days = (int)((new Date().getTime() - first.getTime())/(24 * 60 * 60 * 1000));
        return 1 + days / 7;
    }

    private void setWeekInfo(int now, int max){
        setWeekInfo(now + "/" + max);
    }

    private void setWeekInfo(CharSequence str){
        TextView view = card.findViewById(R.id.testTimeTable_timeInfo);
        view.setText(str);
    }

    private void setWeekSelector(int now, int max){
        ViewGroup group = card.findViewById(R.id.testTimeTable_weekSelector);
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
            group.addView(view);
        }
    }

    private void setWeekTable(int select){
        TableLayout table = card.findViewById(R.id.testTimeTable_tableLayout);
        table.removeAllViews();

        HashSet<TextView> views = new HashSet<>();
        int w_max = 0;
        int h_max = 0;
        {
            Date first = timeTable.firstDay;
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(first);
            calendar.add(Calendar.DATE,select * 7 - 8);

            TableRow row = new TableRow(getContext());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 星期u",Locale.CHINA);
            for(int day = 1;day <= 7;day++){
                calendar.add(Calendar.DATE,1);
                TextView view = new TextView(getContext());
                view.setText(format.format(calendar.getTime()));
                view.setPadding(dp2px(4),dp2px(2),dp2px(4),dp2px(2));
                views.add(view);
                row.addView(view);
            }
            table.addView(row);
        }

        {
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp2px(1),dp2px(2),dp2px(2),dp2px(1));

            for(int time = 1;time < timeTable.getDaySize(); time++){
                TableRow row = new TableRow(getContext());
                for(int day = 1;day <= 7;day ++){
                    final TimeTable.SingleClass single = timeTable.getSingleClass(select,day,time);
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
                        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
                        w_max = Math.max(view.getMeasuredWidth(),w_max);
                        h_max = Math.max(view.getMeasuredHeight(),h_max);
                    }
                    views.add(view);
                    row.addView(view,params);
                }
                row.setPadding(2,2,2,2);
                if(time % 2 == 1) row.setBackgroundColor(0x2F000000);
                getHandler().post(()->table.addView(row));
            }
        }

        for(TextView view : views){
            view.setHeight(h_max);
            view.setWidth(w_max);
        }

    }

    private void setClickListeners() {
        card.findViewById(R.id.testTimeTable_switch).setOnClickListener(this::showTable);
        card.findViewById(R.id.testTimeTable_switch).setClickable(true);
        card.findViewById(R.id.testTimeTable_imageButton).setOnClickListener(this::showSelector);
    }

    private boolean select_on = true;
    private void showTable(View v){
        v.setOnClickListener(this::hideTable);
        card.findViewById(R.id.testTimeTable_imageButton).setVisibility(View.VISIBLE);
        if(select_on) {
            card.findViewById(R.id.testTimeTable_weekSelector).setVisibility(View.VISIBLE);
            card.findViewById(R.id.testTimeTable_divider1).setVisibility(View.VISIBLE);
        }
        card.findViewById(R.id.testTimeTable_divider2).setVisibility(View.VISIBLE);
        card.findViewById(R.id.testTimeTable_tableLayout).setVisibility(View.VISIBLE);
    }

    private void hideTable(View v){
        v.setOnClickListener(this::showTable);
        card.findViewById(R.id.testTimeTable_imageButton).setVisibility(View.GONE);
        card.findViewById(R.id.testTimeTable_divider1).setVisibility(View.GONE);
        card.findViewById(R.id.testTimeTable_weekSelector).setVisibility(View.GONE);
        card.findViewById(R.id.testTimeTable_divider2).setVisibility(View.GONE);
        card.findViewById(R.id.testTimeTable_tableLayout).setVisibility(View.GONE);
    }

    private void showSelector(View v){
        v.setOnClickListener(this::hideSelector);
        ((ImageButton)v).setImageResource(R.drawable.arrow_up_15);
        card.findViewById(R.id.testTimeTable_divider1).setVisibility(View.VISIBLE);
        card.findViewById(R.id.testTimeTable_weekSelector).setVisibility(View.VISIBLE);
        select_on = true;
    }

    private void hideSelector(View v){
        v.setOnClickListener(this::showSelector);
        ((ImageButton)v).setImageResource(R.drawable.arrow_down_15);
        card.findViewById(R.id.testTimeTable_divider1).setVisibility(View.GONE);
        card.findViewById(R.id.testTimeTable_weekSelector).setVisibility(View.GONE);
        select_on = false;
    }

    @Override
    protected int getCardId() {
        return R.id.testTimeTableCard;
    }

    @Override
    protected int getTopToBottom() {
        return R.id.testTimeCard;
    }

    @Override
    protected int getTopToTop() {
        return -1;
    }
}

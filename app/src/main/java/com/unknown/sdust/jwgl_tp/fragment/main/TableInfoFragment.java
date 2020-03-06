package com.unknown.sdust.jwgl_tp.fragment.main;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.DataManager;
import com.unknown.sdust.jwgl_tp.data.store.CalendarStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TableInfoFragment extends Fragment {
    @BindView(R.id.f_table_title)
    TextView titleText;
    @BindView(R.id.f_table_layout)
    TableLayout tableLayout;
    @BindView(R.id.f_table_button)
    ImageButton button;
    @BindView(R.id.f_table_scroll)
    HorizontalScrollView scrollView;

    private final Handler mHandler;
    private DataManager manager = DataManager.getInstance();
    private TableStore table = manager.getTable();
    private CalendarStore calendar = manager.getCalendar();

    private int week_now;

    public TableInfoFragment(Handler mHandler){
        this.mHandler = mHandler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_table,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleText.setText(R.string.loading);
        mHandler.post(this::createView);
    }

    @Override
    public void onStart() {
        super.onStart();
        button.setOnClickListener(this::showTable);
        titleText.setOnClickListener(v->{
            PopupMenu menu = new PopupMenu(Objects.requireNonNull(getContext()),v);
            for (int i = 0;i < table.getWeekSize(); i++) menu.getMenu().add(Menu.NONE,i,i,""+(i + 1));
            menu.setOnMenuItemClickListener((item)->{
                if (item.getItemId() + 1 != week_now){
                    tableLayout.removeViewAt(0);
                    tableLayout.removeViewAt(tableLayout.getChildCount() - 1);
                    for (int i = 0; i < tableLayout.getChildCount(); i++) ((ViewGroup) tableLayout.getChildAt(i)).removeAllViews();
                    createView(item.getItemId() + 1);
                    week_now = item.getItemId() + 1;
                }
                return true;
            });
            menu.show();
        });
    }

    private void createView(){
        if (calendar == null) return;
        int t_week = calendar.getWeekIndex();
        if (t_week > 0 && t_week <= table.getWeekSize()) createView(t_week);
        else Objects.requireNonNull(getActivity()).runOnUiThread(()->{
            titleText.setText(String.format(Locale.ENGLISH,"%d / %d", t_week, table.getWeekSize()));
            titleText.setTextColor(Color.RED);
        });
        week_now = t_week;

    }

    private void createView(int week){
        if (table == null) return;

        List<TableStore.LessonDetail> details = table.getLessons(week);
        Objects.requireNonNull(getActivity()).runOnUiThread(()->{
            //ArrayList<TextView> views = new ArrayList<>(10);
            ArrayList<Integer> indexes = new ArrayList<>(10);
            for (int index = 0; index < 5; index++) indexes.add(index);

            int w_max = 0,h_max = 0;
            int dp4 = ScreenUtil.dp2px(Objects.requireNonNull(getContext()),4);
            for (TableStore.LessonDetail d : details){
                TableRow row = (TableRow) tableLayout.getChildAt(d.getDaytime() % 10 - 1);
                row.setPadding(0,0,dp4,dp4);
                while(row.getChildCount() < d.getDaytime() / 10) row.addView(new TextView(getContext()));


                TextView item = (TextView) row.getChildAt(d.getDaytime() / 10 - 1);
                item.setText(d.getName());
                item.append("\n" + d.getRoom());
                item.setOnClickListener(v-> new AlertDialog.Builder(getContext())
                        .setTitle(d.getName())
                        .setMessage(d.getTeacher() + "\n" + d.getRoom())
                        .setPositiveButton(R.string.ok,null)
                        //.create()
                        .show());

                item.setLayoutParams(createParams(dp4,dp4,dp4,dp4));
                item.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                if (indexes.contains(d.getDaytime() % 10 - 1)) {
                    indexes.remove(Integer.valueOf(d.getDaytime() % 10 - 1));
                }
                w_max = Math.max(item.getMeasuredWidth(),w_max);
                h_max = Math.max(item.getMeasuredHeight(),h_max);
            }
            for (Integer index : indexes) {
                TextView t = new TextView(getContext());
                t.setHeight(h_max);
                ViewGroup g = (ViewGroup) tableLayout.getChildAt(index);
                g.addView(t,createParams(dp4,dp4,dp4,dp4));
            }
            TableRow row = new TableRow(getContext());
            for (int i = 0;i < 7;i++) {
                TextView t = new TextView(getContext());
                t.setPadding(dp4,dp4,dp4,dp4);
                t.setGravity(Gravity.CENTER);
                t.setText(String.valueOf(i + 1));
                t.setWidth(w_max);
                t.setHeight(h_max);
                row.addView(t);
            }
            tableLayout.addView(row,0);
            TextView bz = new TextView(getContext());
            bz.setText(table.getExtra());
            bz.setBackgroundColor(0xaaaaaa);
            bz.setPadding(dp4,dp4,dp4,dp4);
            bz.setHeight(h_max);
            tableLayout.addView(bz);
            titleText.setText(String.format(Locale.ENGLISH,"%d / %d", week, table.getWeekSize()));
        });
    }

    private TableRow.LayoutParams createParams(int l,int t,int r,int b){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(l,t,r,b);
        return params;
    }

    private void showTable(View view){
        view.setOnClickListener(this::hideTable);
        button.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.arrow_up_15));
        scrollView.setVisibility(View.VISIBLE);
    }

    private void hideTable(View view){
        view.setOnClickListener(this::showTable);
        button.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.arrow_down_15));
        scrollView.setVisibility(View.GONE);
    }
}

package com.unknown.sdust.jwgl_tp.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.unknown.sdust.jwgl_tp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeInfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_info,container,false);
        TextView textView = view.findViewById(R.id.f_main_time);
        textView.setText(getTime());
        return view;
    }

    private String getTime(){
        String str = new SimpleDateFormat("yyyy-MM-dd 星期u", Locale.CHINA).format(new Date());
        String[] days = new String[]{"一","二","三","四","五","六","日"};
        str = str.substring(0,str.length() - 1) + days[Integer.parseInt(str.substring(str.length() - 1)) - 1];
        return str;
    }
}

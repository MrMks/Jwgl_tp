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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        return SimpleDateFormat.getDateInstance(DateFormat.FULL).format(new Date());
    }
}

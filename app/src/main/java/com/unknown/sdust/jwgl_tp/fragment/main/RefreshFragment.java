package com.unknown.sdust.jwgl_tp.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.unknown.sdust.jwgl_tp.R;

public class RefreshFragment extends Fragment {
    @StringRes
    private int textID;
    public RefreshFragment(@StringRes int textId){
        textID = textId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refresh_button,container,false);
        TextView text = view.findViewById(R.id.f_main_refresh);
        text.setText(textID);
        return view;
    }
}

package com.unknown.sdust.jwgl_tp.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.unknown.sdust.jwgl_tp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseInfoFragment extends Fragment {

    @BindView(R.id.f_base_account)
    TextView t_account;
    @BindView(R.id.f_base_name)
    TextView t_name;

    private Handler mHandler;
    public BaseInfoFragment(Handler mHandler){
        this.mHandler = mHandler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_info,container,false);
        ButterKnife.bind(this, view);

        t_account.setText(getUserAccount());
        t_name.setText(getUserName());
        return view;
    }

    private String getUserAccount(){
        return "";
    }

    private String getUserName(){
        return "";
    }
}

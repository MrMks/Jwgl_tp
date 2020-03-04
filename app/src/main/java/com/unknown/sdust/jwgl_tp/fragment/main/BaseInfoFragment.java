package com.unknown.sdust.jwgl_tp.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.DataManager;
import com.unknown.sdust.jwgl_tp.data.store.AccountStore;

import java.util.Objects;

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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_info,container,false);
        ButterKnife.bind(this, view);

        mHandler.post(this::updateBaseInfo);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_with_login,menu);
    }

    private void updateBaseInfo(){
        AccountStore store = DataManager.getInstance().getAccount();
        if (store != null){
            Objects.requireNonNull(getActivity()).runOnUiThread(()->{
                t_account.setText(store.getAccount());
                t_name.setText(store.getName());
            });
        }
    }
}
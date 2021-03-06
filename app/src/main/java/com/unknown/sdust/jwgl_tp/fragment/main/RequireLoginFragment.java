package com.unknown.sdust.jwgl_tp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.unknown.sdust.jwgl_tp.Constant;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.activity.LoginActivity;
import com.unknown.sdust.jwgl_tp.data.DataManager;
import com.unknown.sdust.jwgl_tp.data.store.AccountStore;

import java.util.Objects;

public class RequireLoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_require_login,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getView()).setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            AccountStore store = DataManager.getInstance().getAccount();
            if (store != null){
                intent.putExtra(Constant.KEY_ACCOUNT,store.getAccount());
                intent.putExtra(Constant.KEY_PASSWORD,store.getPassword());
                intent.putExtra(Constant.KEY_REMEMBER_PASSWORD,store.isSaved());
            }
            Objects.requireNonNull(getActivity()).startActivityForResult(intent,0);
        });
    }
}

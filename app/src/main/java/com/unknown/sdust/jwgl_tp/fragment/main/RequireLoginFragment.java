package com.unknown.sdust.jwgl_tp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.activity.LoginActivity;

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
            startActivityForResult(intent,0);
        });
    }
}

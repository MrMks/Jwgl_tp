package com.unknown.sdust.jwgl_tp.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.fragment.login.LoginFragment;
import com.unknown.sdust.jwgl_tp.viewModel.VerifyBitmapViewModel;

public class LoginActivity extends AppCompatActivity {
    private HandlerThread thread = new HandlerThread("Login Work Thread");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_main);

        thread.start();
        Handler mHandler = new Handler(thread.getLooper());
        VerifyBitmapViewModel viewModel = new ViewModelProvider(this).get(VerifyBitmapViewModel.class);
        viewModel.setDefault(BitmapFactory.decodeResource(getResources(),R.drawable.randcode_img));

        LoginFragment loginFragment = new LoginFragment(mHandler);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.a_login_content,loginFragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.quitSafely();
    }
}

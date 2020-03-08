package com.unknown.sdust.jwgl_tp.fragment.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.DataManager;
import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.fragment.LoadingFragment;
import com.unknown.sdust.jwgl_tp.viewModel.VerifyBitmapViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.unknown.sdust.jwgl_tp.Constant.KEY_ACCOUNT;
import static com.unknown.sdust.jwgl_tp.Constant.KEY_PASSWORD;
import static com.unknown.sdust.jwgl_tp.Constant.KEY_REMEMBER_PASSWORD;
import static com.unknown.sdust.jwgl_tp.Constant.TAG;

public class LoginFragment extends Fragment {

    @BindView(R.id.f_login_account)
    TextView v_account;
    @BindView(R.id.f_login_password)
    TextView v_password;
    @BindView(R.id.f_login_code)
    TextView v_code;
    @BindView(R.id.f_login_checkBox)
    CheckBox checkBox;
    @BindView(R.id.f_login_imageButton)
    ImageButton imageButton;
    @BindView(R.id.f_login_button)
    Button button;

    private Handler mHandler;
    private VerifyBitmapViewModel viewModel;
    public LoginFragment(Handler mHandler){
        this.mHandler = mHandler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(VerifyBitmapViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_card,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        if (activity == null) return;

        Intent intent = activity.getIntent();
        v_account.setText(intent.getStringExtra(KEY_ACCOUNT));
        v_password.setText(intent.getStringExtra(KEY_PASSWORD));
        checkBox.setChecked(intent.getBooleanExtra(KEY_REMEMBER_PASSWORD,false));
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler.post(this::loadCodeImg);

        imageButton.setOnClickListener(v->{
            v.setClickable(false);
            mHandler.post(this::loadCodeImg);
        });

        button.setOnClickListener(v->{
            String acc = v_account.getText().toString();
            String pass = v_password.getText().toString();
            String code = v_code.getText().toString();
            boolean rem_pass = checkBox.isChecked();

            if (acc.equals("")){
                toast(R.string.cant_empty_acc).run();
            } else if (pass.equals("")){
                toast(R.string.cant_empty_pass).run();
            } else if (code.equals("")) {
                toast(R.string.cant_empty_code).run();
            } else {
                AccountStore store = new AccountStore(acc,pass,rem_pass);
                store.setFromLocal(false);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.a_login_content,new LoadingFragment());
                transaction.addToBackStack("signing in");
                transaction.commit();

                mHandler.post(()->{
                    boolean flag = DataManager.getInstance().login(store,code);
                    if (!flag) {
                        getActivity().runOnUiThread(()-> getActivity().getSupportFragmentManager().popBackStack());
                    } else {
                        getActivity().runOnUiThread(()->{
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(()->{
            Bitmap bitmap = viewModel.getBitmap();
            Objects.requireNonNull(getActivity()).runOnUiThread(()->imageButton.setImageBitmap(bitmap));
        });
    }

    private void loadCodeImg(){
        Bitmap map = null;
        InputStream stream = DataManager.getInstance().getNewCodeImg();
        if(stream != null){
            map = BitmapFactory.decodeStream(stream);
            Matrix matrix = new Matrix();
            matrix.postScale(3,3);
            map = Bitmap.createBitmap(map,0,0,62,22,matrix,true);
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG,e.getMessage(),e);
            }
        }
        if (map != null){
            viewModel.updateBitmap(map);
            Bitmap finalMap = map;
            Objects.requireNonNull(getActivity()).runOnUiThread(()-> {
                imageButton.setImageBitmap(finalMap);
                imageButton.setClickable(true);
            });
        }
        System.gc();
    }

    private Runnable toast(final int resourceId){
        return () -> Toast.makeText(getContext(),resourceId,Toast.LENGTH_SHORT).show();
    }

}

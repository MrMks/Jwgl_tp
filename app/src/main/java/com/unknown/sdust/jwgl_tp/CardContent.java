package com.unknown.sdust.jwgl_tp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.unknown.sdust.jwgl_tp.utils.ResultPack;

public abstract class CardContent<K> implements ICard<K> {
    private AppCompatActivity activity;
    private Handler handler;
    private IInfo<K> info;

    public CardContent(AppCompatActivity activity, IInfo<K> _info){
        this.activity = activity;
        this.handler = new Handler(Looper.getMainLooper());
        setInfo(_info);
    }

    @Override
    public void setInfo(IInfo<K> info) {
        this.info = info;
    }

    protected ResultPack<K> getInfo(){
        return this.info.getInfo();
    }

    protected AppCompatActivity getContext(){
        return activity;
    }
    protected Handler getHandler(){
        return handler;
    }
    protected <T extends View> T findViewById(@IdRes int id){
        return activity.findViewById(id);
    }

    public abstract void run();


    private CardView cardView;

    @Override
    public CardView generate(Context context) {
        if (cardView == null){
            cardView = new CardView(getContext());
            cardView.setRadius(dp2px(4));
            cardView.setLayoutParams(getCardParams());
            cardView.setId(getCardId());
            //getHandler().post(()->((ViewGroup)findViewById(R.id.constraintLayout)).addView(cardView));
        }
        return cardView;
    }

    protected abstract int getCardId();

    private ConstraintLayout.LayoutParams getCardParams(){
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp2px(8),dp2px(16),dp2px(8),dp2px(8));
        params.startToStart = R.id.constraintLayout;
        params.endToEnd = R.id.constraintLayout;
        params.topToTop = getTopToTop();
        params.topToBottom = getTopToBottom();
        return params;
    }

    protected abstract int getTopToBottom();

    protected abstract int getTopToTop();

    protected int dp2px(int dp){
        return Math.round(getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}

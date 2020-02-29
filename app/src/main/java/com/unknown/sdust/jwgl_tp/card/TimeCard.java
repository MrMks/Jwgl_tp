package com.unknown.sdust.jwgl_tp.card;

import android.app.Activity;
import android.widget.TextView;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;

public class TimeCard extends CardContent<String> {
    public TimeCard(Activity activity, IInfo<String> info){
        super(activity,info);
    }

    @Override
    public void run() {
        getHandler().post(()->((TextView)findViewById(R.id.text_time_now)).setText(getInfo().getResult()));
    }

    @Override
    protected int getCardId() {
        return R.id.testTimeCard;
    }

    @Override
    protected int getTopToBottom() {
        return R.id.testCard;
    }

    @Override
    protected int getTopToTop() {
        return -1;
    }
}

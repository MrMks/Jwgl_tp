package com.unknown.sdust.jwgl_tp;

import android.content.Context;

import androidx.cardview.widget.CardView;

interface ICard<T> extends Runnable, IContent<T>{
    CardView generate(Context context);
}

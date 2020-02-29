package com.unknown.sdust.jwgl_tp.viewModel;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

public class VerifyBitmapViewModel extends ViewModel {
    private Bitmap bitmap;
    private Bitmap defaultMap;

    public Bitmap getBitmap() {
        return bitmap == null ? defaultMap : bitmap;
    }

    public void updateBitmap(Bitmap map){
        bitmap = map;
    }

    public void setDefault(Bitmap map){
        defaultMap = map;
    }

    @Override
    protected void onCleared() {
        bitmap = null;
        super.onCleared();
    }
}

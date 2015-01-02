package com.pereiraprive.beerlover;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;


public class LoadingBeerList {

    private boolean dlFinish = false;

    public void setBoolean(boolean dlFinish)
    {
        this.dlFinish = dlFinish;
    }

    public void LoadingBeerList(Activity activity) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(activity, "Please wait ...", "Downloading files ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15000);

                } catch (InterruptedException e) {
                    ringProgressDialog.dismiss();
            }
            }
        }).start();
    }

}

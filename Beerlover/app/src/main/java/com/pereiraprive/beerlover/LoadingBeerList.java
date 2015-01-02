package com.pereiraprive.beerlover;

import android.app.Activity;
import android.app.ProgressDialog;



public class LoadingBeerList {

        private ProgressDialog nDialog;

        protected void onPreExecute(Activity activity) {

            nDialog = new ProgressDialog(activity);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();

        }
}

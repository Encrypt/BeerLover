package com.pereiraprive.beerlover;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.DialogFragment;

public class WorkingFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle("Veuillez patienter...");
        dialog.setMessage("Téléchargement et création de la liste");
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }
}
package com.pereiraprive.beerlover;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class CreditDialogFragment extends DialogFragment {

    static CreditDialogFragment newInstance() {

        String title = "My Fragment";

        CreditDialogFragment f = new CreditDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        Dialog myDialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_launcher)
                .setTitle(title)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity)getActivity()).cancelClicked();
                            }
                        })
                .create();

        return myDialog;
    }
}

package com.pereiraprive.beerlover;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class CreditDialogFragment extends DialogFragment {

    static CreditDialogFragment newInstance() {

        //Local objects
        String title = "Credits";
        String message = "Projet Mobile INF4042\n"+ "Yann Privé & Patrick Pereira\n" + "Licence WTFPL @Binouze\n2014-2015";
        CreditDialogFragment f = new CreditDialogFragment();
        Bundle args = new Bundle();

        //Gets the arguments for the dialog
        args.putString("title", title);
        args.putString("message", message);

        // Create dialog with the previous arguments
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Sets the content of the Dialog
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");

        Dialog myDialog = new AlertDialog.Builder(getActivity())

                // Fills the dialog
                .setIcon(R.drawable.beer)
                .setTitle(title)
                .setMessage(message)

                // Sets the button to close dialog
                .setNegativeButton("Fermer",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })

                // Creates the dialog
                .create();

        return myDialog;
    }
}
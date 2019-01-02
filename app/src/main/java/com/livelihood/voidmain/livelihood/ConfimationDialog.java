package com.livelihood.voidmain.livelihood;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class ConfimationDialog extends AlertDialog{

    Context context;
    String title;
    String message;
    DialogInterface.OnClickListener dialogListener;

    public ConfimationDialog(Context context, String title, String message, DialogInterface.OnClickListener dialogListener) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
        this.dialogListener = dialogListener;




        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("Yes", dialogListener)
                .setNegativeButton("No", dialogListener).setTitle(title).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTitle(title);
        setMessage(message);
        setButton(DialogInterface.BUTTON_POSITIVE, "Yes", dialogListener);
        setButton(DialogInterface.BUTTON_NEGATIVE, "No", dialogListener);
        super.onCreate(savedInstanceState);
    }
}

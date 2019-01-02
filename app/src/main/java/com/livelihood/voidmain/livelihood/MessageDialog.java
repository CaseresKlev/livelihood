package com.livelihood.voidmain.livelihood;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageDialog extends DialogFragment {

    Button ok;
    TextView header, message;
    ImageView icon;

    String strHeader, strMessage;
    int type;


    public MessageDialog() {
    }

    public static  MessageDialog newInstance(String header, String message, int type){
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString("dialogHeader", header);
        args.putString("dialogMessage", message);
        args.putInt("dialogType", type);
        dialog.setArguments(args);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strHeader = getArguments().getString("dialogHeader");
        strMessage = getArguments().getString("dialogMessage");
        type = getArguments().getInt("dialogType");
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.message_dialog, container, false);

        header = (TextView) view.findViewById(R.id.dialog_header);
        header.setText(strHeader);

        message = (TextView) view.findViewById(R.id.dialog_message);
        message.setText(strMessage);
        icon = (ImageView) view.findViewById(R.id.dialog_icon);
        if(type==0){
            icon.setImageResource(R.drawable.ic_information);
        }else{
            icon.setImageResource(R.drawable.warning);
        }

        ok = (Button) view.findViewById(R.id.dialog_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return  view;
    }

    public void setTitle(String title){
        this.header.setText(title);
    }

    public void setMessage(String message){
        this.message.setText(message);
    }



}

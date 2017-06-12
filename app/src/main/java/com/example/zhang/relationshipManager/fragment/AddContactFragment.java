package com.example.zhang.relationshipManager.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.PersonManager;

/**
 * Created by zhang on 2017-06-10.
 */

public class AddContactFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_contact_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        Button add_button = (Button) view.findViewById(R.id.add_button), cancel_button = (Button) view.findViewById(R.id.cancel_button);


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText textInputEditText = (TextInputEditText) view.findViewById(R.id.new_contact_name);
                boolean isSucceed = PersonManager.getInstance(getContext()).addPerson(String.valueOf(textInputEditText.getText()).toString());
                String to_show = isSucceed ? "联系人已成功添加" : "操作失败";
                Snackbar.make(getActivity().getCurrentFocus(), to_show, Snackbar.LENGTH_SHORT).show();
                if (isSucceed) {
                    dismiss();
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}

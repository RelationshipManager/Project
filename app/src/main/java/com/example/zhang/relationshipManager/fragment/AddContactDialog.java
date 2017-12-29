package com.example.zhang.relationshipManager.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhang.relationshipManager.Helper.ToastHelper;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactDataChangeReceiver;
import com.example.zhang.relationshipManager.models.ContactManager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddContactDialog extends DialogFragment {

    @BindView(R.id.bt_confirm)
    Button mBtConfirm;
    @BindView(R.id.bt_cancel)
    Button mBtCancel;
    @BindView(R.id.contact_name)
    EditText mEtName;
    @BindView(R.id.contact_phoneNumber)
    EditText mEtPhoneNumber;

    static public void startDialog(Activity activity) {
        AddContactDialog dialog = new AddContactDialog();
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("add_contact_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();
        dialog.show(fm, "add_contact_dialog");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialog);
        final Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_add_contact, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setLayout(-1, -2);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        ButterKnife.bind(this, view);
        initButtons();
        return view;
    }

    private void initButtons() {
        mBtCancel.setOnClickListener(view -> dismiss());
        mBtConfirm.setOnClickListener(view -> {
            String name = mEtName.getText().toString();
            String phoneNumber = mEtPhoneNumber.getText().toString();
            String resultMsg;
            if ("".equals(name)) {
                resultMsg = "请输入联系人姓名";
            } else {
                Contact contactToAdd = new Contact();
                contactToAdd.setName(name);
                contactToAdd.setPhoneNumber(phoneNumber);
                ContactManager.getInstance(getActivity()).addContact(contactToAdd);
                resultMsg = "添加成功";
                getActivity().sendBroadcast(new Intent(ContactDataChangeReceiver.INTENTFILTER));
                dismiss();
            }
            ToastHelper.show(getActivity(), resultMsg);
        });
    }

    public void show(Activity activity) {
        startDialog(activity);
    }
}

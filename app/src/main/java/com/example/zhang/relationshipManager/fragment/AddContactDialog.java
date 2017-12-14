package com.example.zhang.relationshipManager.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog);
        View view = inflater.inflate(R.layout.dialog_add_contact, container);
        ButterKnife.bind(this, view);
        initButtons();

        return view;
    }

    private void initButtons() {
        mBtCancel.setOnClickListener(view -> dismiss());
        mBtConfirm.setOnClickListener(view -> {
            String name = mEtName.getText().toString();
            String resultMsg;
            if ("".equals(name)) {
                resultMsg = "请输入联系人姓名";
            } else {
                Contact contactToAdd = new Contact();
                contactToAdd.setName(name);
                ContactManager.getInstance().addContact(contactToAdd);
                resultMsg = "添加成功";
            }
            Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
        });
    }

    public void show() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("add_contact_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        super.show(getFragmentManager(), "add_contact_dialog");
    }
}

package com.example.zhang.relationshipManager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ContactInfoFragment extends BaseFragment {

    @BindView(R.id.contact_name_textView)
    TextView contactNameTextView;
    @BindView(R.id.contact_sex_textView)
    TextView contactSexTextView;
    @BindView(R.id.contact_age_textView)
    TextView contactAgeTextView;
    @BindView(R.id.contact_mobilenumber_textView)
    TextView contactMobilenumberTextView;
    @BindView(R.id.contact_note_textView)
    TextView contactNoteTextView;
    @BindView(R.id.contact_name)
    EditText contactName;
    @BindView(R.id.contact_sex_spinner)
    Spinner contactSexSpinner;
    @BindView(R.id.contact_age)
    EditText contactAge;
    @BindView(R.id.contact_mobilenumber)
    EditText contactMobilenumber;
    @BindView(R.id.contact_note)
    EditText contactNote;
    @BindView(R.id.contactInfo_viewSwitcher)
    ViewSwitcher viewSwitcher;
    Unbinder unbinder;

    public ContactInfoFragment setmContact(int mContactId) {
        mContact = ContactManager.getInstance(getContext()).getContactById(mContactId);
        return this;
    }

    private Contact mContact;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_contactinfo;
    }

    @Override
    protected void initViews() {
    }

    public void startEditContact() {
        viewSwitcher.showNext();
    }

    public void deleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String contact_name = contactName.getText().toString();
        builder.setTitle("确认删除联系人 " + contact_name + " ？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isSucceed = ContactManager.getInstance(getContext()).removeContact(mContact);
                String to_show = isSucceed ? "删除成功" : "操作失败";
                Snackbar.make(mView, to_show, Snackbar.LENGTH_SHORT).show();
            }
        }).setNegativeButton("取消", null);
        builder.show();
    }

    public void endEditContact() {
        mContact.setAge(Integer.valueOf(contactAge.getText().toString()));
        mContact.setName(contactName.getText().toString());
        mContact.setPhoneNumber(contactMobilenumber.getText().toString());
        mContact.setSex(contactSexSpinner.getSelectedItem().equals("男") ? Contact.SEX_MALE : Contact.SEX_FEMALE);
        mContact.setNotes(contactNote.getText().toString());
        ContactManager.getInstance(getContext()).updateContact(mContact);
        viewSwitcher.showPrevious();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

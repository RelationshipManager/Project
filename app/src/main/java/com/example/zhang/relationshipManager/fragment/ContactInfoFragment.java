package com.example.zhang.relationshipManager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.zhang.relationshipManager.Helper.ToastHelper;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactDataChangeReceiver;
import com.example.zhang.relationshipManager.models.ContactManager;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    private boolean isEditing = false;

    public ContactInfoFragment setmContact(int mContactId) {
        mContact = ContactManager.getInstance(getContext()).getContactById(mContactId);
        return this;
    }

    private Contact mContact;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_contactinfo;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (!isVisibleToUser && isEditing){
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setTitle("当前修改的联系人信息还未保存，是否需要保存？").setPositiveButton("保存", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ContactManager.getInstance(getContext()).updateContact(mContact);
//                }
//            }).setNegativeButton("取消", null);
//            builder.show();
//        }
//    }

    @Override
    protected void initViews() {
        ArrayAdapter<String> genderTypeAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        genderTypeAdapter.addAll("男", "女", "保密");
        genderTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        contactSexSpinner.setAdapter(genderTypeAdapter);
        setContactInfo();
    }

    private void setContactInfo(){
        contactAge.setText(mContact.getAge() == -1 ? "" : String.valueOf(mContact.getAge()));
        contactMobilenumber.setText(mContact.getPhoneNumber());
        contactName.setText(mContact.getName());
        contactNote.setText(mContact.getNotes());
        contactAgeTextView.setText(mContact.getAge() == -1 ? "未填写" : String.valueOf(mContact.getAge()));
        contactMobilenumberTextView.setText(mContact.getPhoneNumber().equals(Contact.DEFAULT_PHONE_NUM) ? "未填写" : mContact.getPhoneNumber());
        contactNameTextView.setText(mContact.getName());
        contactSexTextView.setText(mContact.getSexStr());
        contactNoteTextView.setText(mContact.getNotes().equals(Contact.DEFAULT_NOTES) ? "未填写" : mContact.getNotes());
    }

    public void startEditContact() {
        if (isEditing){
            endEditContact();
            setContactInfo();
            viewSwitcher.showPrevious();
        }else {
            viewSwitcher.showNext();
        }
        isEditing = !isEditing;
    }

    public void deleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String contact_name = contactName.getText().toString();
        builder.setTitle("确认删除联系人 " + contact_name + " ？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isSucceed = ContactManager.getInstance(getContext()).removeContact(mContact);
                String to_show = isSucceed ? "删除成功" : "操作失败";
                ToastHelper.show(getContext(), to_show);
                if (isSucceed) {
                    getActivity().finish();
                    getContext().sendBroadcast(new Intent(ContactDataChangeReceiver.INTENTFILTER));
                }
            }
        }).setNegativeButton("取消", null);
        builder.show();
    }

    public void endEditContact() {
        String name = contactName.getText().toString();
        if ("".equals(name))
            ToastHelper.show(getActivity(), "联系人姓名不得为空");
        String age = contactAge.getText().toString();
        int sex = Contact.SEX_SECRET;
        switch ((String)contactSexSpinner.getSelectedItem()){
            case "男":
                sex = Contact.SEX_MALE;
            case "女":
                sex = Contact.SEX_FEMALE;
        }
        mContact.setAge("".equals(age)?Contact.DEFAULT_AGE:Integer.valueOf(age));
        mContact.setName(name);
        mContact.setPhoneNumber(contactMobilenumber.getText().toString());
        mContact.setSex(sex);
        mContact.setNotes(contactNote.getText().toString());
        ContactManager.getInstance(getContext()).updateContact(mContact);
        getContext().sendBroadcast(new Intent(ContactDataChangeReceiver.INTENTFILTER));
    }
}

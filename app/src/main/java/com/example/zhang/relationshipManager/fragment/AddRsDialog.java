package com.example.zhang.relationshipManager.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zhang.relationshipManager.Helper.ToastHelper;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactManager;
import com.example.zhang.relationshipManager.models.Relationship;
import com.example.zhang.relationshipManager.models.RelationshipManager;
import com.example.zhang.relationshipManager.models.RsDataChangeReceiver;
import com.example.zhang.relationshipManager.models.RsType;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddRsDialog extends DialogFragment {
    @BindView(R.id.bt_confirm)
    Button mBtConfirm;
    @BindView(R.id.bt_cancel)
    Button mBtCancel;
    @BindView(R.id.spinner_start_contact)
    Spinner mSpStartContact;
    @BindView(R.id.spinner_end_contact)
    Spinner mSpEndContact;
    @BindView(R.id.spinner_rs_start_roles)
    Spinner mSpStartRoles;
    @BindView(R.id.spinner_rs_end_roles)
    Spinner mSpEndRoles;

    //联系人列表
    private ArrayList<Contact> mContactList;
    //角色列表
    private ArrayList<String> mAllRolesList;
    //待添加关系
    private Relationship mRelationship;

    static public void startDialog(Activity activity){
        AddRsDialog dialog = new AddRsDialog();
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("add_rs_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();
        dialog.show(fm, "add_rs_dialog");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialog);
        View view = inflater.inflate(R.layout.dialog_add_rs, container, false);
        ButterKnife.bind(this, view);
        mContactList = ContactManager.getInstance(null).getAllContacts();
        mAllRolesList = RelationshipManager.getInstance(null).getAllRoles();
        mRelationship = new Relationship();
        initButtons();
        initSpinners();
        setSpinners();
        return view;
    }

    private void initSpinners(){
        mSpStartContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRelationship.setStartContact(mContactList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpEndContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRelationship.setEndContact(mContactList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpStartRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRelationship.getRsType().setStartRole(mAllRolesList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpEndRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRelationship.getRsType().setEndRole(mAllRolesList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void initButtons() {
        mBtCancel.setOnClickListener(view -> dismiss());
        mBtConfirm.setOnClickListener(view -> {
            String resultMsg;
            if (mRelationship.getStartContact().getId() == Contact.DEFAULT_ID || mRelationship.getEndContact().getId() == Contact.DEFAULT_ID){
                resultMsg = "请选择联系人";
            } else if (mRelationship.getStartContact() == mRelationship.getEndContact()){
                // TODO: 2017/12/25 应该从另一个列表中删去已被选中的联系人
                resultMsg = "关系双方不能会是同一个联系人";
            } else {
                RsType rt = mRelationship.getRsType();
                if ("".equals(rt.getStartRole()) || "".equals(rt.getEndRole())){
                    resultMsg = "请选择角色";
                }else {
                    if (RelationshipManager.getInstance(null).addRelationship(mRelationship)){
                        resultMsg = "添加成功";
                        getActivity().sendBroadcast(new Intent(RsDataChangeReceiver.INTENTFILTER));
                        dismiss();
                    }
                    else
                        // TODO: 2017/12/25 应该自动更新另一个列表中的角色，避免类型对应不上
                        resultMsg = "添加失败,选择角色类型有误";
                }
            }
            ToastHelper.show(getActivity(), resultMsg);
        });
    }

    private void setSpinners(){
        ArrayList<String> nameList = getContactName(mContactList);

        ArrayAdapter<String> contactStartAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_contact_item, R.id.spinner_contact_item, nameList);
        contactStartAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        mSpStartContact.setAdapter(contactStartAdapter);

        ArrayAdapter<String> contactEndAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_contact_item, R.id.spinner_contact_item, nameList);
        contactEndAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        mSpEndContact.setAdapter(contactEndAdapter);

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_contact_item, R.id.spinner_contact_item, mAllRolesList);
        roleAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        mSpStartRoles.setAdapter(roleAdapter);
        mSpEndRoles.setAdapter(roleAdapter);
    }

    private ArrayList<String> getContactName(ArrayList<Contact> contactList){
        ArrayList<String> names = new ArrayList<>();
        for (Contact c :
                contactList) {
            names.add(c.getName());
        }
        return names;
    }

    public void show(Activity activity) {
        startDialog(activity);
    }
}

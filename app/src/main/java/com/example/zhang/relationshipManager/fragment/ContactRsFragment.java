package com.example.zhang.relationshipManager.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhang.relationshipManager.Helper.ToastHelper;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactManager;
import com.example.zhang.relationshipManager.models.Relationship;
import com.example.zhang.relationshipManager.models.RelationshipManager;
import com.example.zhang.relationshipManager.models.RsDataChangeReceiver;

import java.util.ArrayList;

import butterknife.BindView;


public class ContactRsFragment extends BaseFragment {
    @BindView(R.id.contactRsRecyclerView)
    RecyclerView contactRsRecyclerView;
    @BindView(R.id.bt_add_rs)
    FloatingActionButton mBtAddRs;

    private Contact mContact;
    private RsDataChangeReceiver mRsDataChangeReceiver;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_contactrs;
    }

    @Override
    protected void initViews() {
        contactRsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        contactRsRecyclerView.setAdapter(new ContactRsAdapter(RelationshipManager.getInstance(null).getRelationships(mContact)));
        mBtAddRs.setOnClickListener(view -> AddRsDialog.startDialog(getActivity()));
        mRsDataChangeReceiver = new RsDataChangeReceiver(getContext(), () ->
                ((ContactRsAdapter) contactRsRecyclerView.getAdapter())
                        .update(RelationshipManager.getInstance(null).getRelationships(mContact)));
    }

    public ContactRsFragment setContact(Contact contact){
        mContact=contact;
        return this;
    }

    private class ContactRsAdapter extends RecyclerView.Adapter<ContactRsFragment.ContactRsAdapter.ViewHolder> {

        private ArrayList<Relationship> mContactRsList;


        ContactRsAdapter(ArrayList<Relationship> contactRsList) {
            mContactRsList = contactRsList;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
            TextView contactName;
            TextView contactRelationship;

            ViewHolder(View view) {
                super(view);
                view.setOnLongClickListener(this);
                contactName = (TextView) view.findViewById(R.id.contact_name);
                contactRelationship = (TextView) view.findViewById(R.id.contact_relationship);
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                String contact_name = contactName.getText().toString();
                builder.setTitle("确认删除关系 " + contact_name + " ？").setPositiveButton("删除", (dialog, which) -> {

                    boolean isSucceed = RelationshipManager.getInstance(null).removeRelationship(mContactRsList.get(getAdapterPosition()));
                    String to_show = isSucceed ? "删除成功" : "操作失败";
                    ToastHelper.show(getContext(), to_show);
                    if (isSucceed) {
                        getContext().sendBroadcast(new Intent(RsDataChangeReceiver.INTENTFILTER));
                    }
                }).setNegativeButton("取消", null);
                builder.show();
                return true;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contactrs_item, parent, false);
//            view.setOnClickListener(this);
            ContactRsFragment.ContactRsAdapter.ViewHolder holder = new ContactRsFragment.ContactRsAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Relationship relationship=mContactRsList.get(position);
            if(relationship.getStartContact()==mContact) {
                holder.contactName.setText(relationship.getEndContact().getName());
                holder.contactRelationship.setText(relationship.getRsType().getEndRole());
            }
            else if(relationship.getEndContact()==mContact) {
                holder.contactName.setText(relationship.getStartContact().getName());
                holder.contactRelationship.setText(relationship.getRsType().getStartRole());
            }
        }

        @Override
        public int getItemCount() {
            return mContactRsList.size();
        }

        public void update(ArrayList<Relationship> contactRsList) {
            mContactRsList = contactRsList;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRsDataChangeReceiver.unRegister();
    }
}

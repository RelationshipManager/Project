package com.example.zhang.relationshipManager.fragment;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.Relationship;
import com.example.zhang.relationshipManager.models.RelationshipManager;

import java.util.ArrayList;

import butterknife.BindView;


public class ContactRsFragment extends BaseFragment {
    @BindView(R.id.contactRsRecyclerView)
    RecyclerView contactRsRecyclerView;
    private Contact mContact;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_contactrs;
    }

    @Override
    protected void initViews() {
        contactRsRecyclerView.setAdapter(new ContactRsAdapter(RelationshipManager.getInstance(null).getRelationships(mContact)));
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

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView contactName;
            TextView contactRelationship;

            ViewHolder(View view) {
                super(view);
                contactName = (TextView) view.findViewById(R.id.contact_name);
                contactRelationship = (TextView) view.findViewById(R.id.contact_relationship);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contactrs_item, parent, false);
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
    }
}

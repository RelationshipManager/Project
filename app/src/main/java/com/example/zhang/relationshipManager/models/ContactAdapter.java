package com.example.zhang.relationshipManager.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.activities.ContactInfoActivity;

import java.util.ArrayList;

/**
 * Created by 29110 on 2017/12/11.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ArrayList<Contact> mContactList;

    public ContactAdapter(ArrayList<Contact> contactList) {
        mContactList = contactList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView contactImage;
        TextView contactName;
        View contactView;

        public ViewHolder(View view) {
            super(view);
            contactView = view;
            contactImage = (ImageView) view.findViewById(R.id.contact_image);
            contactName = (TextView) view.findViewById(R.id.contact_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        // @todo comment for test
//        holder.contactImage.setImageResource(contact.getImageId());
        holder.contactName.setText(contact.getName());
        holder.contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactInfoActivity.startActivity(v.getContext(), mContactList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}

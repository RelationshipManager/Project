package com.example.zhang.relationshipManager.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;

import java.util.ArrayList;
import java.util.List;

import com.example.zhang.relationshipManager.models.Contact;

/**
 * Created by 29110 on 2017/12/11.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ArrayList<Contact> mContactList;

    public ContactAdapter(ArrayList<Contact> contactlist){
        mContactList=contactlist;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView contactimage;
        TextView contactName;

        public ViewHolder(View view){
            super(view);
            contactimage=(ImageView)view.findViewById(R.id.contact_image);
            contactName=(TextView)view.findViewById(R.id.contact_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact=mContactList.get(position);
        holder.contactimage.setImageResource(contact.getImageId());
        holder.contactName.setText(contact.getName());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}

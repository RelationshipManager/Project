package com.example.zhang.relationshipManager.models;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.fragment.ShowContactFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Person} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private final List<Person> mValues;
    //    private final OnListFragmentInteractionListener mListener;
    private final View mView;

    public ContactListAdapter(List<Person> items, View view) {
        mValues = items;
        mView = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.contactName.setText(mValues.get(position).name);
        holder.contactImage.setImageResource(R.drawable.ic_person_grey600_36dp);

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("确认删除联系人？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(mView, "删除成功", Snackbar.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", null);
                builder.show();
                return false;
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView contactName;
        public final de.hdodenhof.circleimageview.CircleImageView contactImage;
        public Person mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            contactName = (TextView) view.findViewById(R.id.contactName);
            contactImage = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.contactImage);
        }
    }
}

package com.example.zhang.relationshipManager.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

<<<<<<< HEAD:app/src/main/java/com/example/zhang/relationshipManager/models/ContactListAdapter.java
import com.example.zhang.relationshipManager.models.ShowContactFragment.OnListFragmentInteractionListener;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.ShowContactFragment.OnListFragmentInteractionListener;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.ShowContactFragment.OnListFragmentInteractionListener;
=======
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.ShowContactFragment.OnListFragmentInteractionListener;

>>>>>>> 1c8654bf5807a78f773018a7fbc3f7c51dd40a98:app/src/main/java/com/example/zhang/relationshipManager/models/ContactListAdapter.java
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Person} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private final List<Person> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ContactListAdapter(List<Person> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
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

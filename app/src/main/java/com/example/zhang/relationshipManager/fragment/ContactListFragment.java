package com.example.zhang.relationshipManager.fragment;

import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.activities.ContactInfoActivity;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactDataChangeReceiver;
import com.example.zhang.relationshipManager.models.ContactManager;

import java.util.ArrayList;

import butterknife.BindView;

public class ContactListFragment extends BaseFragment {
    @BindView(R.id.contactRecyclerView)
    RecyclerView contactRecyclerView;

    ContactDataChangeReceiver contactDataChangeReceiver;

    @Override
    protected int getResourceId() {
        return R.layout.contact_fragment;
    }

    @Override
    protected void initViews() {
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        contactRecyclerView.setAdapter(new ContactAdapter(ContactManager.getInstance(getContext()).getAllContacts()));
        contactDataChangeReceiver = new ContactDataChangeReceiver(getContext(), new ContactDataChangeReceiver.Refreshable() {
            @Override
            public void refresh() {
                ((ContactAdapter) contactRecyclerView.getAdapter()).update();
            }
        });
        getContext().registerReceiver(contactDataChangeReceiver, new IntentFilter(ContactDataChangeReceiver.INTENTFILTER));
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

        private ArrayList<Contact> mContactList;

        ContactAdapter(ArrayList<Contact> contactList) {
            mContactList = contactList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView contactImage;
            TextView contactName;
            View contactView;

            ViewHolder(View view) {
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
//          holder.contactImage.setImageResource(contact.getImageId());
            holder.contactName.setText(contact.getName());
            holder.contactView.setOnClickListener(v ->
                    ContactInfoActivity.startActivity(v.getContext(), mContactList.get(holder.getAdapterPosition()).getId()));
        }

        @Override
        public int getItemCount() {
            return mContactList.size();
        }

        public void update() {
            mContactList = ContactManager.getInstance(getContext()).getAllContacts();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactDataChangeReceiver.unRegister();
    }
}

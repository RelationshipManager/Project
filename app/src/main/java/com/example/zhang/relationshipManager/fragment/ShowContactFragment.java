package com.example.zhang.relationshipManager.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.activities.BaseActivity;
import com.example.zhang.relationshipManager.models.ContactListAdapter;
import com.example.zhang.relationshipManager.models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ShowContactFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    //    private OnListFragmentInteractionListener mListener;
    private List<Person> contactLIst = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactListAdapter contactListAdapter;
    private View view;
    private Activity mActivity;
    private Context mContext;
    public class DataChanged_BroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w("Receive Broadcast","onReceive");
            Initial();
            recyclerView.setAdapter(contactListAdapter);
        }
    }

    public DataChanged_BroadcastReceiver dataChanged_broadcastReceiver;

    public ShowContactFragment() {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the mActivity and potentially other fragments contained in that
     * mActivity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ShowContactFragment newInstance(int columnCount) {
        ShowContactFragment fragment = new ShowContactFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    private void Initial() {
        contactLIst = BaseActivity.getPersonManager().getAllPerson();
        contactListAdapter = new ContactListAdapter(contactLIst);
        contactListAdapter.setContext(mContext).setView(view).setActivity(mActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mActivity = getActivity();
        mContext = getContext();

        Initial();
        // Set the adapter

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(contactListAdapter);
        return view;
    }

    /**
     * Called when a fragment is first attached to its mContext.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter intentFilter = new IntentFilter("DataChanged");
        dataChanged_broadcastReceiver = new DataChanged_BroadcastReceiver();
        context.registerReceiver(dataChanged_broadcastReceiver,intentFilter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton addBtn = (FloatingActionButton) getActivity().findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactFragment addContactFragment = new AddContactFragment();
                addContactFragment.setmView(view);
                addContactFragment.setmContext(getContext());
                addContactFragment.show(getFragmentManager(), "添加新的联系人");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Person item);
    }
}

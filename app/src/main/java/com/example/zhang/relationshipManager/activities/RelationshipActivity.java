package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.fragment.AddRelationshipFragment;
import com.example.zhang.relationshipManager.models.Person;
import com.example.zhang.relationshipManager.models.Relationship;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelationshipActivity extends BaseActivity {

    @BindView(R.id.contact_name)
    AppCompatTextView mContactName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textView_contactID)
    AppCompatTextView mTextViewContactID;
    @BindView(R.id.relationship_list)
    RecyclerView mRelationshipList;

    //显示的联系人
    private Person mPerson;
    //列表
    private ArrayList<Relationship> mRelationships;
    //适配器
    private RelationshipListAdapter mAdapter;

    static public void startActivity(Context context, Person person){
        Intent intent=new Intent(context,RelationshipActivity.class);
        intent.putExtra("person",person);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        mPerson=(Person)getIntent().getSerializableExtra("person");
        mContactName.setText(mPerson.getName());
        mTextViewContactID.setText(String.valueOf(mPerson.getId()));


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRelationships=getRelationshipManager().getRelationshipsOfPerson(mPerson);
        mAdapter=new RelationshipListAdapter(mRelationships);
        mRelationshipList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RelationshipActivity.this,mRelationships.get(position).getRelationshipType(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void refreshList(){
        mRelationships=getRelationshipManager().getRelationshipsOfPerson(mPerson);
        mAdapter.updateList(mRelationships);
        mAdapter.notify();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.relationship_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_relationship:
                new AddRelationshipFragment().setAttri(this,mPerson).show("addRelationship");
                break;
            // @// TODO: 2017-06-12 让返回键起效
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class RelationshipListAdapter extends RecyclerView.Adapter<RelationshipListAdapter.ViewHolder>{

        private ArrayList<Relationship> mRelationships;
        private OnItemClickListener mOnItemClickListener;

        RelationshipListAdapter(ArrayList<Relationship> relationships) {
            mRelationships=relationships;
        }

        void updateList(ArrayList<Relationship> relationships) {
            mRelationships = relationships;
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            mOnItemClickListener=listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view= LayoutInflater.from(RelationshipActivity.this).inflate(R.layout.relationship_list_item,viewGroup,false);
            ViewHolder viewHolder=new ViewHolder(view);
            viewHolder.mRelationshipType=(TextView) view.findViewById(R.id.relationship_type);
            viewHolder.mContactName=(TextView) view.findViewById(R.id.contact_name);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder,int i) {
            Relationship relationship=mRelationships.get(i);
            viewHolder.mContactName.setText(relationship.getTargetPerson().getName());
            viewHolder.mRelationshipType.setText(relationship.getRelationshipType());
            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mRelationships.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView mRelationshipType;
            public TextView mContactName;
            public ViewHolder(View view){
                super(view);
            }
        }
    }

}

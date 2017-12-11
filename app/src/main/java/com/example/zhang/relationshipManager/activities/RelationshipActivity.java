package com.example.zhang.relationshipManager.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
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
import com.example.zhang.relationshipManager.models.DataChangeReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelationshipActivity extends BaseActivity {

    @BindView(R.id.contact_name)
    AppCompatTextView mContactName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.relationship_list)
    RecyclerView mRelationshipList;

    //显示的联系人
    private Person mPerson;
    //列表
    private ArrayList<Relationship> mRelationships;
    //适配器
    private RelationshipListAdapter mAdapter;
    //数据变更广播接收器
    private DataChangeReceiver mDataChangeReceiver;

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

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //卸载广播
        mDataChangeReceiver.unRegister();
    }

    private void init(){
        mPerson=(Person)getIntent().getSerializableExtra("person");
        mContactName.setText(mPerson.getName());

        //构造广播接收器
        mDataChangeReceiver=new DataChangeReceiver(this,new DataChangeReceiver.Refreshable() {
            @Override
            public void refresh() {
                refreshList();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRelationships=getRelationshipManager().getRelationshipsOfPerson(mPerson);
        mAdapter=new RelationshipListAdapter(mRelationships);
        mRelationshipList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UpdateRelationshipFragment fragment=new UpdateRelationshipFragment();
                fragment.setAttri(RelationshipActivity.this,mPerson,mRelationships.get(position)).show("updateRelationship");
            }
        });
        mAdapter.setOnLongClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RelationshipActivity.this);
                builder.setTitle("确认删除关系？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean isSucceed = getRelationshipManager().removeRelationship(mRelationships.get(position));
                        String to_show = isSucceed ? "删除成功" : "操作失败";
                        Snackbar.make(view, to_show, Snackbar.LENGTH_SHORT).show();
                        RelationshipActivity.this.sendBroadcast(new Intent("DataChanged"));
                    }
                }).setNegativeButton("取消", null);
                builder.show();
            }
        });
    }

    public void refreshList(){
        mRelationships=getRelationshipManager().getRelationshipsOfPerson(mPerson);
        mAdapter.updateList(mRelationships);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
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


    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class RelationshipListAdapter extends RecyclerView.Adapter<RelationshipListAdapter.ViewHolder>{

        private ArrayList<Relationship> mRelationships;
        private OnItemClickListener mOnItemClickListener;
        private OnItemClickListener mOnItemLongClickListener;

        RelationshipListAdapter(ArrayList<Relationship> relationships) {
            mRelationships=relationships;
        }

        void updateList(ArrayList<Relationship> relationships) {
            mRelationships = relationships;
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            mOnItemClickListener=listener;
        }

        public void setOnLongClickListener(OnItemClickListener listener){
            mOnItemLongClickListener=listener;
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
            viewHolder.mRelationshipType.setText(relationship.getTargetRole());
            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                });
            }
            if(mOnItemLongClickListener!=null){
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemLongClickListener.onItemClick(viewHolder.itemView,viewHolder.getAdapterPosition());
                        return true;
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


    static public class AddRelationshipFragment extends ChangeRelationshipFragment{
        @Override
        public void init() {
            mBtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Person targetPerson= getPersonManager().getPersonByName(mTargetPersonName.getText().toString());
                    if(getRelationshipManager().addRelationship(mPerson,targetPerson,
                            (String)mSourcePersonRole.getSelectedItem(),
                            (String)mTargetPersonRole.getSelectedItem())){
                        Intent intent=new Intent("DataChanged");
                        getActivity().sendBroadcast(intent);
                        Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(),"添加失败",Toast.LENGTH_SHORT).show();
                    }
                    mTargetPersonName.setText("");
                }
            });
        }
    }

    static public class UpdateRelationshipFragment extends ChangeRelationshipFragment{
        private Relationship mRelationship;

        public ChangeRelationshipFragment setAttri(Activity activity, Person person,Relationship relationship){
            super.setAttri(activity,person);
            mRelationship=relationship;
            return this;
        }

        @Override
        public void init() {
            mSourcePersonRole.setSelection(mRoles.indexOf(mRelationship.getSourceRole()));
            mTargetPersonRole.setSelection(mRoles.indexOf(mRelationship.getTargetRole()));
            mTargetPersonName.setText(mRelationship.getTargetPerson().getName());
            mBtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelationshipManager relationshipManager=getRelationshipManager();
                    String targetName=mTargetPersonName.getText().toString();
                    String sourceRole=(String)mSourcePersonRole.getSelectedItem();
                    String targetRole=(String)mTargetPersonRole.getSelectedItem();
                    if(targetName.equals(mRelationship.getTargetPerson().getName())){
                        if(relationshipManager.updateRelationship(mRelationship,sourceRole,targetRole)){
                            Intent intent=new Intent("DataChanged");
                            getActivity().sendBroadcast(intent);
                            Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Person targetPerson= getPersonManager().getPersonByName(targetName);
                        if(relationshipManager.addRelationship(mPerson,targetPerson,
                                sourceRole,targetRole)){
                            relationshipManager.removeRelationship(mRelationship);
                            Intent intent=new Intent("DataChanged");
                            getActivity().sendBroadcast(intent);
                            Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}

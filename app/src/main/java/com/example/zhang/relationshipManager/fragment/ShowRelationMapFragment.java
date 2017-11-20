package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.activities.RelationshipActivity;
import com.example.zhang.relationshipManager.models.DataChangeReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowRelationMapFragment extends Fragment {

    //二级列表
    @BindView(R.id.expandable_list_view)
    public ExpandableListView mExpandableListView;
    //工具栏
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    //数据列表
    private ArrayList<PersonList> mPersonLists;
    //适配器
    private PersonExpandableListAdapter mAdapter;
    //数据变化广播接收器
    private DataChangeReceiver mDataChangeReceiver;

    public static ShowRelationMapFragment newInstance() {
        return new ShowRelationMapFragment();
    }

    public ShowRelationMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_relation_map, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //卸载广播
        mDataChangeReceiver.unRegister();
    }

    private void init(){
        mDataChangeReceiver=new DataChangeReceiver(getActivity(),new DataChangeReceiver.Refreshable() {
            @Override
            public void refresh() {
                refreshList();
            }
        });

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("关系图谱");

        mPersonLists=new ArrayList<>();
        refreshList();
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                RelationshipActivity.startActivity(getActivity(),mPersonLists.get(i).get(i1));
                return false;
            }
        });
    }

    private void refreshList(){
        RelationshipManager relationshipManager=RelationshipManager.getInstance(getActivity());
        mPersonLists.clear();
        int maxLevel= relationshipManager.getMaxLevel();
        int minLevel= relationshipManager.getMinLevel();
        for(int i=maxLevel;i>=minLevel;i--){
            mPersonLists.add(new PersonList(i,relationshipManager.getPersonsByLevel(i)));
        }
        if(mAdapter==null)
            mAdapter=new PersonExpandableListAdapter(mPersonLists);
        else
            mAdapter.updateList(mPersonLists);

    }

    public class PersonList{
        int mLevel;
        String mLevelName;
        ArrayList<Person> mPersons;
        PersonList(int level){
            mLevel=level;
            mLevelName="等级"+String.valueOf(level);
            mPersons=new ArrayList<>();
        }

        PersonList(int level,ArrayList<Person> persons){
            mLevel=level;
            mLevelName="等级"+String.valueOf(level);
            mPersons=persons;
        }

        public void add(Person person){
            mPersons.add(person);
        }

        public Person get(int index){
            return mPersons.get(index);
        }

        public int size(){
            return mPersons.size();
        }

        public int getLevel() {
            return mLevel;
        }

        public String getLevelName() {
            return mLevelName;
        }
    }

    private class PersonExpandableListAdapter extends BaseExpandableListAdapter {


        ArrayList<PersonList> mPersonLists;
        LayoutInflater mInflater;

        public PersonExpandableListAdapter(ArrayList<PersonList> personLists) {
            mInflater=LayoutInflater.from(getActivity());
            mPersonLists=personLists;
        }

        public void updateList(ArrayList<PersonList> personLists) {
            mPersonLists=personLists;
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return mPersonLists.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return mPersonLists.get(i).size();
        }

        @Override
        public Object getGroup(int i) {
            return mPersonLists.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return mPersonLists.get(i).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view!=null){
                holder=(ViewHolder)view.getTag();
            }else{
                view= mInflater.inflate(R.layout.level_list_item,viewGroup,false);
                holder=new ViewHolder();
                holder.mTextView=(TextView)view.findViewById(R.id.level_name);
                view.setTag(holder);
            }
            holder.mTextView.setText(mPersonLists.get(i).getLevelName());
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view!=null){
                holder=(ViewHolder)view.getTag();
            }else{
                view= mInflater.inflate(R.layout.contact_list_item,null);
                holder=new ViewHolder();
                holder.mTextView=(TextView)view.findViewById(R.id.contactName);
                view.setTag(holder);
            }
            holder.mTextView.setText(mPersonLists.get(i).get(i1).getName());
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        private class ViewHolder{
            CircleImageView mImageView;
            TextView mTextView;
        }
    }

}

package com.example.jgj.myapplication3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jgj on 2015/10/14.
 */
public class ParentAdpter extends BaseAdapter {
    private Context context;
    private ArrayList<TestModel> testModels;
    private  ListView listView;
    public ParentAdpter(Context context,ListView listView,ArrayList<TestModel> testModels) {
        this.listView = listView;
        this.testModels = testModels;
        this.context = context;

    }

    @Override
    public int getCount() {
        return testModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("getView",position+"");
        ViewHolder viewHolder = null;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.parent_adpter, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(testModels.get(position).tv);
        viewHolder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateItemView(position);
//                testModels.get(position).tv = "点击啦";
            }
        });
//        ListView listView = (ListView) convertView.findViewById(R.id.in_listview);
//        listView.setAdapter(new InAdpter());
//        setListViewHeightBasedOnChildren(listView);
//        }else {
//
//        }

        return convertView;
    }
    private class ViewHolder{
        private TextView tv;
    }
    public void updateItemView(int position) {
        //得到第一个可显示控件的位置，
        int visiblePosition = listView.getFirstVisiblePosition();
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        int index = position - visiblePosition;
        if (index >= 0) {
            //得到要更新的item的view
            View view = listView.getChildAt(index);
            //从view中取得holder
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.tv.setText("测试数据");
            holder.tv.setBackground(context.getResources().getDrawable(R.mipmap.ic_launcher));
            testModels.get(index).tv = "点击啦";
        }
    }
    private class InAdpter extends BaseAdapter{

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.in_adpter,null);
            return convertView;
        }
    }
}

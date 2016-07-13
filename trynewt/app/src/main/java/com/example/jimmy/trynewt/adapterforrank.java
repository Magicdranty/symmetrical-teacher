package com.example.jimmy.trynewt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class adapterforrank extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater = null;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();

    public adapterforrank(Context context, List<DataHolder> datalist) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (datalist != null && datalist.size() > 0) {
            mDataList.addAll(datalist);
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.itemforrank, null);
            holder.no = (TextView) convertView.findViewById(R.id.ininno);
            holder.img = (ImageView) convertView.findViewById(R.id.ininimg);
            holder.title = (TextView) convertView.findViewById(R.id.inintitle);
            holder.score=(TextView) convertView.findViewById(R.id.score);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        DataHolder item = mDataList.get(position);
        if(position==0){ holder.img.setImageResource(R.drawable.crown);}
        holder.title.setText(item.studentid);
        holder.no.setText(String.valueOf(position + 1));
        holder.score.setText(String.valueOf(item.score));
        return convertView;
    }

    public static class ViewHolder {
        TextView no, title,score;
        ImageView img;
    }

    public static class DataHolder {
        public String  studentid;
        int score;
    }
}
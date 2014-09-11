package com.huassit.imenu.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Comment;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment> commentList = new ArrayList<Comment>();
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private int layout=0;
    private DisplayImageOptions options;
    private SimpleDateFormat sdf =new SimpleDateFormat("MM/dd HH:mm");

    public CommentAdapter(Context context,int layout) {
        this.context = context;
        this.layout = layout;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        options = MyApplication.getDisplayImageOptions(context,20);
    }



    public ArrayList<Comment> getDataList() {
        return commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder h = new ViewHolder();
        if (v == null) {
            v = inflater.inflate(layout, null);
            h.member_pic = (ImageView) v.findViewById(R.id.member_pic);
            h.member_name = (TextView) v.findViewById(R.id.member_name);
            h.time = (TextView) v.findViewById(R.id.time);
            h.comment = (TextView) v.findViewById(R.id.comment);
            v.setTag(h);
        } else {
            h = (ViewHolder) v.getTag();
        }
        Comment t = (Comment) getItem(position);
        imageLoader.displayImage(t.icon_location + t.icon_name, h.member_pic,options);
        h.member_name.setText(t.member_name);
        h.time.setText(sdf.format(TimeUtil.toDate(t.comment_date)));
        h.comment.setText(t.comment_content);
        return v;
    }

    
    
    class ViewHolder {
        ImageView member_pic;
        TextView member_name;
        TextView time;
        TextView comment;
    }

}

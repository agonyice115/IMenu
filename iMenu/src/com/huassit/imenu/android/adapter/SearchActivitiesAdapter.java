package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Dynamic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchActivitiesAdapter extends AbsAdapter<Dynamic> {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public SearchActivitiesAdapter(Activity context, int layout) {
        super(context, layout);
        imageLoader = ImageLoader.getInstance();
        options = MyApplication.getDisplayImageOptions(context, 40);
    }

    @Override
    public ViewHolder<Dynamic> getHolder() {
        return new ActivityViewHolder();
    }

    private class ActivityViewHolder implements ViewHolder<Dynamic> {

        private ImageView memberIcon;
        private TextView memberName;
        private TextView memberActivity;
        private TextView likeCount;
        private TextView commentCount;


        @Override
        public void initViews(View v, int position) {
            memberIcon = (ImageView) v.findViewById(R.id.icon);
            memberName = (TextView) v.findViewById(R.id.memberName);
            memberActivity = (TextView) v.findViewById(R.id.memberActivity);
            likeCount = (TextView) v.findViewById(R.id.likeCount);
            commentCount = (TextView) v.findViewById(R.id.commentCount);
        }

        @Override
        public void updateDatas(Dynamic dynamic, int position) {
            imageLoader.displayImage(dynamic.getMemberInfo().iconLocation + dynamic.getMemberInfo().iconName, memberIcon, options);
            memberName.setText(dynamic.getMemberInfo().memberName);
            memberActivity.setText(dynamic.getTitle());
            likeCount.setText(dynamic.getGoodsCount());
            commentCount.setText(dynamic.getCommentCount());
        }

        @Override
        public void doOthers(Dynamic dynamic, int position) {

        }
    }
}

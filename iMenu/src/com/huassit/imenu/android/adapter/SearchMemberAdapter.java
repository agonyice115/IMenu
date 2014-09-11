package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Member;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchMemberAdapter extends AbsAdapter<Member> {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private SearchMemberCallback callback;

    public SearchMemberAdapter(Activity context, int layout, SearchMemberCallback callback) {
        super(context, layout);
        imageLoader = ImageLoader.getInstance();
        options = MyApplication.getDisplayImageOptions(context, 40);
        this.callback = callback;
    }

    @Override
    public ViewHolder<Member> getHolder() {
        return new MemberViewHolder();
    }

    private class MemberViewHolder implements ViewHolder<Member> {

        private ImageView memberIcon;
        private TextView memberName;
        private TextView memberSignature;
        private TextView memberPeople;
        private TextView followedStatus;

        @Override
        public void initViews(View v, int position) {
            memberIcon = (ImageView) v.findViewById(R.id.icon);
            memberName = (TextView) v.findViewById(R.id.memberName);
            memberSignature = (TextView) v.findViewById(R.id.memberSignature);
            memberPeople = (TextView) v.findViewById(R.id.memberPeople);
            followedStatus = (TextView) v.findViewById(R.id.goImageView);
        }

        @Override
        public void updateDatas(final Member member, int position) {
            imageLoader.displayImage(member.iconLocation + member.iconName, memberIcon, options);
            memberName.setText(member.memberName);
            memberSignature.setText(member.signature);
            memberPeople.setText(member.followedCount);
            final String statue = FollowListAdapter
					.showStatus(member.followStatus);
            followedStatus.setText(statue);
            followedStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onFollowButtonClick(member);
                }
            });
        }

        @Override
        public void doOthers(Member member, int position) {

        }
    }

    public static interface SearchMemberCallback {
        public void onFollowButtonClick(Member member);
    }
    
}

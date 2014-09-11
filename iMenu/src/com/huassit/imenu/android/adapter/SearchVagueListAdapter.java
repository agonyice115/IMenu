package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.SearchVague;

/**
 * Created by Sylar on 14-7-15.
 */
public class SearchVagueListAdapter extends AbsAdapter<SearchVague> {
    public SearchVagueListAdapter(Activity context, int layout) {
        super(context, layout);
    }

    @Override
    public ViewHolder<SearchVague> getHolder() {
        return new VagueViewHolder();
    }

    private class VagueViewHolder implements ViewHolder<SearchVague> {
        private TextView resultName;
        private TextView resultCount;

        @Override
        public void initViews(View v, int position) {
            resultCount = (TextView) v.findViewById(R.id.resultCount);
            resultName = (TextView) v.findViewById(R.id.resultName);
        }

        @Override
        public void updateDatas(SearchVague searchVague, int position) {
            resultName.setText(searchVague.resultName);
            resultCount.setText(context.getString(R.string.vague_result_count, searchVague.resultCount));
        }

        @Override
        public void doOthers(SearchVague searchVague, int position) {

        }
    }
}

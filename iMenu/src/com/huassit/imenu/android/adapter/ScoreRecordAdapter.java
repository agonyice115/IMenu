package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Score;

/**
 * 积分记录适配器
 * 
 * @author shang_guan
 * 
 */
public class ScoreRecordAdapter extends AbsAdapter<Score> {
	public ScoreRecordAdapter(Activity context, int layout) {
		super(context, layout);
	}

	@Override
	public ViewHolder<Score> getHolder() {
		return new ScoreViewHolder();
	}

	private final class ScoreViewHolder implements ViewHolder<Score> {

		/** 积分标题 */
		private TextView score_title;
		/** 积分详细 */
		private TextView score_description;
		/** 积分数量 */
		private TextView score_num;
		/** 积分时间 */
		private TextView score_date;

		@Override
		public void initViews(View v, int position) {
			score_title = (TextView) v.findViewById(R.id.score_title);
			score_description = (TextView) v
					.findViewById(R.id.score_description);
			score_num = (TextView) v.findViewById(R.id.score);
			score_date = (TextView) v.findViewById(R.id.score_date);
		}

		@Override
		public void updateDatas(Score score, int position) {
			score_title.setText(score.score_title);
			score_description.setText(score.score_description);
			score_num.setText(score.score);
			score_date.setText(score.score_date);
		}

		@Override
		public void doOthers(Score score, int position) {

		}
	}
}

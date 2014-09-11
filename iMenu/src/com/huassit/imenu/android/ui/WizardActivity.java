package com.huassit.imenu.android.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-3.
 */
public class WizardActivity extends BaseActivity {
    private ViewPager viewPager;
    private int[] wizardImages = new int[]{
            R.drawable.wizard_1, R.drawable.wizard_2, R.drawable.wizard_3, R.drawable.wizard_4};
    private List<ImageView> imageViewList;

    @Override
    public int getContentView() {
        return R.layout.wizard_activity;
    }

    @Override
    public void initView() {
        imageViewList = new ArrayList<ImageView>(4);
        for (int i = 0; i < wizardImages.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(wizardImages[i]);
            if (i == wizardImages.length - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
            imageViewList.add(imageView);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(imageViewList.get(position));
                return imageViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        viewPager.setCurrentItem(0);
    }

    @Override
    public void initData() {

    }
}

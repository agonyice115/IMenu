package com.huassit.imenu.android.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.AreaCodeAdapter;
import com.huassit.imenu.android.db.dao.AreaCodeDao;
import com.huassit.imenu.android.model.AreaCode;
import com.huassit.imenu.android.view.NavigationView;

import java.util.List;

/**
 * Created by Sylar on 14-7-3.
 */
public class SelectedAreaCodeActivity extends BaseActivity {

    private NavigationView navigationView;
    private ListView areaCodeListView;
    private AreaCodeDao areaCodeDao;
    private AreaCodeAdapter areaCodeAdapter;

    @Override
    public int getContentView() {
        return R.layout.select_area_code_activity;
    }

    @Override
    public void initView() {
        areaCodeDao = new AreaCodeDao(this);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW, R.string.select_area_code);
        navigationView.setNavigateItemVisibility(NavigationView.LEFT_IMAGE_VIEW, View.GONE);
        navigationView.setNavigateItemVisibility(NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
        navigationView.setNavigateItemBackground(NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
        navigationView.setNavigateItemOnClickListener(NavigationView.SECOND_RIGHT_TEXT_VIEW, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
            }
        });
        areaCodeListView = (ListView) findViewById(R.id.area_code_list_view);
        areaCodeAdapter = new AreaCodeAdapter(this, R.layout.area_code_list_item);
        areaCodeListView.setAdapter(areaCodeAdapter);
        areaCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AreaCode areaCode = (AreaCode) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("selectedAreaCode", areaCode);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
            }
        });
    }

    @Override
    public void initData() {
        AreaCode selectedAreaCode = (AreaCode) getIntent().getSerializableExtra("selectedAreaCode");
        List<AreaCode> areaCodeList = areaCodeDao.getAreaCodeList();
        if (areaCodeList != null) {
            for (AreaCode areaCode : areaCodeList) {
                if (selectedAreaCode.equals(areaCode)) {
                    areaCode.isSelected = true;
                    break;
                }
            }
            areaCodeAdapter.getDataList().addAll(areaCodeList);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
    }
}

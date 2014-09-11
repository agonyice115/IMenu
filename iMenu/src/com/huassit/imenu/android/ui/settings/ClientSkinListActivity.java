package com.huassit.imenu.android.ui.settings;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.ClientSkinListAdapter;
import com.huassit.imenu.android.db.dao.ClientSkinDao;
import com.huassit.imenu.android.model.ClientSkin;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

import java.util.List;

/**
 * Created by Sylar on 14-7-11.
 */
public class ClientSkinListActivity extends BaseActivity {
    private NavigationView navigationView;
    private ListView mClientSkinListView;
    private ClientSkinListAdapter mAdapter;
    private ClientSkinDao mSkinDao;
    private List<ClientSkin> clientSkinList;

    @Override
    public int getContentView() {
        return R.layout.client_skin_activity;
    }

    @Override
    public void initView() {
        mSkinDao = new ClientSkinDao(this);
        clientSkinList = mSkinDao.getAllSkin();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigateItemText(NavigationView.TITLE, R.string.select_theme);
        navigationView.setNavigateItemVisibility(NavigationView.LEFT_IMAGE_VIEW, View.GONE);
        navigationView.setNavigateItemVisibility(NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
        navigationView.setNavigateItemBackground(NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
        navigationView.setNavigateItemBackground(NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
        navigationView.setCurrentCategoryTriangle(NavigationView.TRIANGLE_MINE);
        navigationView.setNavigateItemOnClickListener(NavigationView.LEFT_TEXT_VIEW, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mClientSkinListView = (ListView) findViewById(R.id.clientSkinListView);
        mAdapter = new ClientSkinListAdapter(this, R.layout.client_skin_list_item);
        mClientSkinListView.setAdapter(mAdapter);
        mClientSkinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientSkin skin = (ClientSkin) parent.getItemAtPosition(position);
                for (ClientSkin clientSkin : clientSkinList) {
                    clientSkin.isSelected = skin == clientSkin;
                }
                navigationView.setBackgroundColor(Color.parseColor(skin.client_skin_value));
                PreferencesUtils.putString(context, "CurrentSkinId", skin.client_skin_id);
                PreferencesUtils.putString(context, "ColorValue", skin.client_skin_value);
                mAdapter.getDataList().clear();
                mAdapter.getDataList().addAll(clientSkinList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void initData() {
        String currentSkinId = PreferencesUtils.getString(this, "CurrentSkinId");
        if (!StringUtils.isBlank(currentSkinId)) {
            for (ClientSkin skin : clientSkinList) {
                if (skin.client_skin_id.equals(currentSkinId)) {
                    skin.isSelected = true;
                } else {
                    skin.isSelected = false;
                }
            }
        }
        mAdapter.getDataList().addAll(clientSkinList);
        mAdapter.notifyDataSetChanged();
    }
}

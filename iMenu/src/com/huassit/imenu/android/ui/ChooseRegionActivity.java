package com.huassit.imenu.android.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.util.StringUtils;

public class ChooseRegionActivity extends BaseActivity {

	private Spinner sp_province;
	private Spinner sp_city;
	String pro;
	ArrayAdapter<CharSequence> cityAdapter;

	@Override
	public int getContentView() {

		return R.layout.choose_region;
	}

	@Override
	protected void onResume() {
		setTitle(StringUtils.getString(this, R.string.choose_region));
		super.onResume();
	}

	@Override
	public void initView() {
		sp_province = (Spinner) findViewById(R.id.province);
		sp_city = (Spinner) findViewById(R.id.city);

	}

	@Override
	public void initData() {
		ArrayAdapter<CharSequence> proAdapter = ArrayAdapter
				.createFromResource(context, R.array.province,
						android.R.layout.simple_spinner_item);
		proAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cityAdapter = ArrayAdapter.createFromResource(context,
				R.array.default_item, android.R.layout.simple_spinner_item);
		sp_province.setAdapter(proAdapter);
		sp_city.setAdapter(cityAdapter);
		sp_province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				pro = (String) spinner.getItemAtPosition(position);
				if ("安徽".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.anhui,
									android.R.layout.simple_spinner_item);
				} else if ("陕西".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.shaanxi,
							android.R.layout.simple_spinner_item);
				} else if ("澳门".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.aomen,
									android.R.layout.simple_spinner_item);
				} else if ("北京".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.beijing,
							android.R.layout.simple_spinner_item);
				} else if ("重庆".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.chongqing,
							android.R.layout.simple_spinner_item);
				} else if ("福建".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.fujian,
							android.R.layout.simple_spinner_item);
				} else if ("广东".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.guangdong,
							android.R.layout.simple_spinner_item);
				} else if ("甘肃".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.gansu,
									android.R.layout.simple_spinner_item);
				} else if ("广西".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.guangxi,
							android.R.layout.simple_spinner_item);
				} else if ("贵州".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.guizhou,
							android.R.layout.simple_spinner_item);
				} else if ("湖北".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.hubei,
									android.R.layout.simple_spinner_item);
				} else if ("河北".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.hebei,
									android.R.layout.simple_spinner_item);
				} else if ("黑龙江".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.heilongjiang,
							android.R.layout.simple_spinner_item);
				} else if ("湖南".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.hunan,
									android.R.layout.simple_spinner_item);
				} else if ("河南".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.henan,
									android.R.layout.simple_spinner_item);
				} else if ("海南".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.hainan,
							android.R.layout.simple_spinner_item);
				} else if ("吉林".equals(pro)) {
					cityAdapter = ArrayAdapter
							.createFromResource(context, R.array.jilin,
									android.R.layout.simple_spinner_item);
				} else if ("江苏".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.jiangsu,
							android.R.layout.simple_spinner_item);
				} else if ("江西".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.jiangxi,
							android.R.layout.simple_spinner_item);
				} else if ("辽宁".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.liaoning,
							android.R.layout.simple_spinner_item);
				} else if ("内蒙古".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.neimenggu,
							android.R.layout.simple_spinner_item);
				} else if ("宁夏".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.ningxia,
							android.R.layout.simple_spinner_item);
				} else if ("青海".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.qinghai,
							android.R.layout.simple_spinner_item);
				} else if ("四川".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.sichuan,
							android.R.layout.simple_spinner_item);
				} else if ("山东".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.shandong,
							android.R.layout.simple_spinner_item);
				} else if ("上海".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.shanghai,
							android.R.layout.simple_spinner_item);
				} else if ("山西".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.shanxi,
							android.R.layout.simple_spinner_item);
				} else if ("天津".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.tianjing,
							android.R.layout.simple_spinner_item);
				} else if ("台湾".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.taiwan,
							android.R.layout.simple_spinner_item);
				} else if ("西藏".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.xizang,
							android.R.layout.simple_spinner_item);
				} else if ("香港".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.xianggang,
							android.R.layout.simple_spinner_item);
				} else if ("新疆".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.xinjiang,
							android.R.layout.simple_spinner_item);
				} else if ("云南".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.yunnan,
							android.R.layout.simple_spinner_item);
				} else if ("浙江".equals(pro)) {
					cityAdapter = ArrayAdapter.createFromResource(context,
							R.array.zhejiang,
							android.R.layout.simple_spinner_item);
				}
				sp_city.setAdapter(cityAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String city = (String) parent.getItemAtPosition(position);
				if (!city.equals(StringUtils.getString(
						ChooseRegionActivity.this, R.string.choose_city))) {
					Intent intent = new Intent(ChooseRegionActivity.this,
							UserInfoActivity.class);
					intent.putExtra("province", pro);
					intent.putExtra("city", city);
					setResult(RESULT_OK, intent);
					finish();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}

}

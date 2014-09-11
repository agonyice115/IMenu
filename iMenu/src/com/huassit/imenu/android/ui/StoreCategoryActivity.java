package com.huassit.imenu.android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.*;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.ServiceListAdapter;
import com.huassit.imenu.android.adapter.StoreCategoryAdapter;
import com.huassit.imenu.android.adapter.StoreRegionAdapter;
import com.huassit.imenu.android.db.dao.CategoryDao;
import com.huassit.imenu.android.db.dao.RegionDao;
import com.huassit.imenu.android.db.dao.ServiceDao;
import com.huassit.imenu.android.model.Category;
import com.huassit.imenu.android.model.Region;
import com.huassit.imenu.android.model.Service;
import com.huassit.imenu.android.util.LocationUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.SegmentControllerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-30.
 */
public class StoreCategoryActivity extends BaseActivity {
	public static final String PARENT_LEVEL = "1";
	public static final String OPEN = "1";
	private NavigationView mNavigationView;
	private ListView mCategoryListView;
	private ListView mRegionListView;
	private ListView serviceListView;
	private CategoryDao categoryDao;
	private RegionDao regionDao;
	private ServiceDao serviceDao;
	private StoreCategoryAdapter mCategoryAdapter;
	private StoreRegionAdapter mRegionAdapter;
	private ServiceListAdapter serviceListAdapter;
	private LinearLayout regionCityLayout;
	private TextView regionCityTextView;
	private GeoCoder mGeoCoder;
	private Region currentRegion;
	private Spinner provinceSpinner;
	private Spinner citySpinner;
	private TextView headViewTitle;
	private View headView;
	private ImageView headIcon;
	private Category selectedCategory;
	private Service selectedService;
	private ImageView icon;
	private String currentRegionString;
	private SegmentControllerView segmentControllerView;
	/** 存储省份 */
	private String text;
	private String currentCity;
	private String currentCityId;

	@Override
	public int getContentView() {
		return R.layout.store_category_activity;
	}

	@Override
	protected void onResume() {
		super.onResume();
		segmentControllerView.setColor(Color.parseColor(colorValue));
		// 回到页面时显示还原上次segment状态
		if (!StringUtils.isBlank(PreferencesUtils.getString(context,
				"segmentPosition"))) {
			segmentControllerView.setPosition(Integer.valueOf(PreferencesUtils
					.getString(context, "segmentPosition")));

		}
	}

	@Override
	public void initView() {
		categoryDao = new CategoryDao(this);
		regionDao = new RegionDao(this);
		serviceDao = new ServiceDao(this);
		mGeoCoder = GeoCoder.newInstance();
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.store);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_IMAGE_VIEW, null);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_IMAGE_VIEW, R.drawable.triangle_up);
		mNavigationView
				.setNavigateItemBackground(
						NavigationView.SECOND_RIGHT_TEXT_VIEW,
						R.drawable.select_normal);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 离开页面时存储所选数据
						PreferencesUtils.putString(context, "segmentPosition",
								segmentControllerView.getPosition() + "");
						PreferencesUtils.putString(context,
								"ServiceCurrentIndex",
								serviceListAdapter.currentIndex + "");
						PreferencesUtils.putString(context,
								"CategoryCurrentIndex",
								mCategoryAdapter.currentIndex + "");
						if (selectedCategory!=null && selectedCategory.category_id!=null) {
							PreferencesUtils.putString(context, "CategoryId",
									selectedCategory.category_id);
						}
						PreferencesUtils.putString(context,
								"RegionCurrentIndex",
								mRegionAdapter.currentIndex + "");
						if (currentRegion!=null && currentRegion.region_id!=null) {
							PreferencesUtils.putString(context, "RegionId",
									currentRegion.region_id);
						}

						Intent intent = new Intent(context, MainActivity.class);
						intent.putExtra("sourceActivity",
								"StoreCategoryActivity");
						if (mRegionListView.isShown()) {
							if (currentRegion != null) {
								intent.putExtra("Region",
										currentRegion.region_id);
							}
						} else if (mCategoryListView.isShown()) {
							if (selectedCategory != null) {
								intent.putExtra("Category",
										selectedCategory.category_id);
							}
						} else if (serviceListView.isShown()) {
							if (selectedService != null) {
								intent.putExtra("Service",
										selectedService.serviceId);
							}
						}
						startActivity(intent);
						overridePendingTransition(R.anim.translate_in,
								R.anim.translate_out);
					}
				});

		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		icon = (ImageView) findViewById(R.id.icon);
		regionCityLayout = (LinearLayout) findViewById(R.id.regionCodeLayout);
		regionCityLayout.setVisibility(View.GONE);
		regionCityTextView = (TextView) findViewById(R.id.regionCityTextView);

		segmentControllerView = (SegmentControllerView) findViewById(R.id.segmentView);
		segmentControllerView
				.setOnChangeListener(new SegmentControllerView.OnChangeListener() {
					@Override
					public void onChange() {
						switch (segmentControllerView.getPosition()) {
						case 0:
							// 分类
							// mCategoryAdapter.currentIndex = -1;
							if (!StringUtils.isBlank(PreferencesUtils
									.getString(context, "CategoryCurrentIndex"))) {
								mCategoryAdapter.currentIndex = Integer.valueOf(PreferencesUtils
										.getString(context,
												"CategoryCurrentIndex"));
								mCategoryAdapter.notifyDataSetChanged();
							}
							mCategoryListView.setVisibility(View.VISIBLE);
							mRegionListView.setVisibility(View.GONE);
							serviceListView.setVisibility(View.GONE);
							regionCityLayout.setVisibility(View.GONE);
							headIcon.setVisibility(View.INVISIBLE);
							headViewTitle
									.setText(getString(R.string.category_all));
							initCategory();
							if (!StringUtils.isBlank(PreferencesUtils.getString(context, "CategoryId"))) {
								String categoryId = PreferencesUtils.getString(context, "CategoryId");
								Category category = categoryDao.getCategoryById(categoryId);
								selectedCategory = category;
								// 如果是父类中的子项 展开父类
								if (!StringUtils.isBlank(category.parent_id)) {
									Category father = findFatherCategoryByChild(category);
									if (null != father
											&& null != father.children
											&& father.children.size() > 0) {
										mCategoryAdapter.getDataList().clear();
										mCategoryAdapter.getDataList().addAll(
												father.children);
									}
								}
							}
							break;
						case 1:
							// 地区
							if (!StringUtils.isBlank(PreferencesUtils
									.getString(context, "RegionCurrentIndex"))) {
								mRegionAdapter.currentIndex = Integer
										.valueOf(PreferencesUtils.getString(
												context, "RegionCurrentIndex"));
								mRegionAdapter.notifyDataSetChanged();
							}
							icon.setBackgroundResource(R.drawable.map_pressed);
							mRegionListView.setVisibility(View.VISIBLE);
							mCategoryListView.setVisibility(View.GONE);
							serviceListView.setVisibility(View.GONE);
							regionCityLayout.setVisibility(View.VISIBLE);
							headIcon.setVisibility(View.VISIBLE);
							regionCityTextView
									.setOnClickListener(regionCityClickListener);
							if (currentRegion != null) {
								headViewTitle
										.setText(currentCity);
							}
							regionCityTextView.setText(currentRegionString);
							if (!StringUtils.isBlank(PreferencesUtils
									.getString(context, "RegionId"))) {
								String regionId = PreferencesUtils.getString(
										context, "RegionId");
								Region region = regionDao
										.getRegionById(regionId);
								currentRegion = region;
								fillRegionListView();
								// 如果是父类中的子项 展开父类
								// 某个区下面的某个地方的level为4 例如钟楼
								if (region.children==null && "4".equals(region.level)) {
									List<Region> children = findFatherRegionByChild(region);
									if (null != children && children.size() > 0) {
										mRegionAdapter.getDataList().clear();
										mRegionAdapter.getDataList().addAll(
												children);
										mRegionAdapter.notifyDataSetChanged();
									}
								}
							}
							break;
						case 2:
							// 服务
							if (!StringUtils.isBlank(PreferencesUtils
									.getString(context, "ServiceCurrentIndex"))) {
								serviceListAdapter.currentIndex = Integer.valueOf(PreferencesUtils
										.getString(context,
												"ServiceCurrentIndex"));
								serviceListAdapter.notifyDataSetChanged();
							}
							headIcon.setVisibility(View.INVISIBLE);
							headViewTitle
									.setText(getString(R.string.category_all));
							mRegionListView.setVisibility(View.GONE);
							mCategoryListView.setVisibility(View.GONE);
							serviceListView.setVisibility(View.VISIBLE);
							regionCityLayout.setVisibility(View.GONE);
							regionCityTextView.setOnClickListener(null);
							break;
						}
					}
				});
		segmentControllerView.init(getResources().getStringArray(
				R.array.store_category_segment_items));
		segmentControllerView.setPosition(0);
		segmentControllerView.draw();

		headView = getLayoutInflater().inflate(
				R.layout.region_category_header_view, null);
		headViewTitle = (TextView) headView.findViewById(R.id.headViewTitle);
		headViewTitle.setText(getString(R.string.category_all));
		headIcon = (ImageView) headView.findViewById(R.id.icon);
		headViewTitle.setTextColor(getResources().getColor(R.color.white));
		headView.setBackgroundColor(Color.parseColor(colorValue));
		headView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCategoryListView.isShown()) {
					initCategory();
					mCategoryAdapter.currentIndex = -1;
					selectedCategory =null;
					PreferencesUtils.putString(context, "CategoryCurrentIndex", mCategoryAdapter.currentIndex+"");
					PreferencesUtils.putString(context, "CategoryId", "");
					mCategoryAdapter.notifyDataSetChanged();
				} else if (mRegionListView.isShown()) {
					fillRegionListView();
					mRegionAdapter.currentIndex = -1;
					currentRegion =null;
					PreferencesUtils.putString(context, "RegionCurrentIndex", mRegionAdapter.currentIndex+"");
					PreferencesUtils.putString(context, "RegionId",
							currentCityId);
					mRegionAdapter.notifyDataSetChanged();
				} else if (serviceListView.isShown()) {
					initService();
					serviceListAdapter.currentIndex = -1;
					PreferencesUtils.putString(context, "ServiceCurrentIndex", serviceListAdapter.currentIndex+"");
					serviceListAdapter.notifyDataSetChanged();
				}
			}
		});
		mRegionListView = (ListView) findViewById(R.id.regionListView);
		mRegionAdapter = new StoreRegionAdapter(this, R.layout.simple_list_item);
		mRegionListView.addHeaderView(headView);
		mRegionListView.setAdapter(mRegionAdapter);
		mRegionListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Region region = (Region) parent
								.getItemAtPosition(position);
						if (region.children != null
								&& region.children.size() > 0) {
							mRegionAdapter.currentIndex = -1;
							mRegionAdapter.getDataList().clear();
							mRegionAdapter.getDataList()
									.addAll(region.children);
							mRegionAdapter.notifyDataSetChanged();
						} else {
							mRegionAdapter.currentIndex = position - 1;
							mRegionAdapter.notifyDataSetChanged();
							currentRegion = region;
							PreferencesUtils.putString(context,
									"RegionCurrentIndex",
									mRegionAdapter.currentIndex + "");
							PreferencesUtils.putString(context, "RegionId",
									currentRegion.region_id);
						}
					}
				});

		mCategoryListView = (ListView) findViewById(R.id.categoryListView);
		mCategoryAdapter = new StoreCategoryAdapter(this,
				R.layout.simple_list_item);
		mCategoryListView.addHeaderView(headView);
		mCategoryListView.setAdapter(mCategoryAdapter);
		mCategoryListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Category category = (Category) parent
								.getItemAtPosition(position);
						if (category.level.equals(PARENT_LEVEL)
								&& category.children.size() > 0) {
							mCategoryAdapter.currentIndex = -1;
							mCategoryAdapter.getDataList().clear();
							mCategoryAdapter.getDataList().addAll(
									category.children);
							mCategoryAdapter.notifyDataSetChanged();
						} else {
							selectedCategory = category;
							mCategoryAdapter.currentIndex = position - 1;
							mCategoryAdapter.notifyDataSetChanged();
							PreferencesUtils.putString(context,
									"CategoryCurrentIndex",
									mCategoryAdapter.currentIndex + "");
							PreferencesUtils.putString(context, "CategoryId",
									selectedCategory.category_id);
						}
					}
				});

		serviceListView = (ListView) findViewById(R.id.serviceListView);
		serviceListAdapter = new ServiceListAdapter(this,
				R.layout.simple_list_item);
		serviceListView.addHeaderView(headView);
		serviceListView.setAdapter(serviceListAdapter);
		serviceListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Service service = (Service) parent
								.getItemAtPosition(position);
						selectedService = service;
						serviceListAdapter.currentIndex = position - 1;
						serviceListAdapter.notifyDataSetChanged();
						PreferencesUtils.putString(context,
								"ServiceCurrentIndex",
								serviceListAdapter.currentIndex + "");
					}
				});
	}

	private View.OnClickListener regionCityClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			View view = getLayoutInflater().inflate(
					R.layout.province_city_view, null);
			provinceSpinner = (Spinner) view.findViewById(R.id.provinceSpinner);
			citySpinner = (Spinner) view.findViewById(R.id.citySpinner);
			final List<Region> provinceList = regionDao.getProvinceList();
			final ArrayAdapter<Region> provinceAdapter = new ArrayAdapter<Region>(
					StoreCategoryActivity.this,
					android.R.layout.simple_spinner_dropdown_item, provinceList);
			provinceSpinner.setAdapter(provinceAdapter);
			provinceSpinner
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							Region region = (Region) parent
									.getItemAtPosition(position);
							final List<Region> cityList = regionDao
									.getCityList(region.region_id);
							ArrayAdapter<Region> cityAdapter = new ArrayAdapter<Region>(
									StoreCategoryActivity.this,
									android.R.layout.simple_spinner_dropdown_item,
									cityList);
							citySpinner.setAdapter(cityAdapter);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});
			new AlertDialog.Builder(StoreCategoryActivity.this)
					.setTitle(R.string.please_select)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									currentRegion = (Region) citySpinner
											.getSelectedItem();
									if (OPEN.equals(currentRegion.is_open)) {
										fillRegionListView();
									} else {
										new AlertDialog.Builder(
												StoreCategoryActivity.this)
												.setTitle(R.string.prompt)
												.setMessage(
														getString(R.string.service_not_available))
												.setPositiveButton(
														R.string.confirm,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																dialog.dismiss();

															}
														}).create().show();
									}
								}
							}).setView(view).create().show();
		}
	};

	@Override
	public void initData() {
		initCurrentCity();
		initCategory();
		initService();
	}

	private void initService() {
		List<Service> serviceList = serviceDao.getServiceList();
		if (serviceList != null && serviceList.size() > 0) {
			serviceListAdapter.getDataList().clear();
			serviceListAdapter.getDataList().addAll(serviceList);
			serviceListAdapter.notifyDataSetChanged();
		}
	}

	private void initCurrentCity() {
		BDLocation location = LocationUtils.getInstance(this).getLocation();
		if (location != null) {
			mGeoCoder
					.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
						@Override
						public void onGetGeoCodeResult(
								GeoCodeResult geoCodeResult) {

						}

						@Override
						public void onGetReverseGeoCodeResult(
								ReverseGeoCodeResult reverseGeoCodeResult) {
							if (reverseGeoCodeResult == null
									|| reverseGeoCodeResult.error != ReverseGeoCodeResult.ERRORNO.NO_ERROR) {
								regionCityTextView
										.setText(R.string.please_select);
								mRegionListView.setVisibility(View.GONE);
							} else {
								ReverseGeoCodeResult.AddressComponent component = reverseGeoCodeResult
										.getAddressDetail();
								String text1 = getString(R.string.please_choose);
								if (!StringUtils.isBlank(component.province)
										&& component.province.length() > 2) {
									text = component.province.substring(0, 2);
								}
								if (!StringUtils.isBlank(component.city)
										&& component.city.length() > 2) {
									String city = component.city
											.substring(0, 2);
									text1 = text + "/" + city;
									// 临时代码 目前为“西安1”
									currentRegion = regionDao
											.getRegionByName(city + "1");
									//存储当前城市以及icon
									PreferencesUtils.putString(context, "CurrentCity",currentRegion.region_name);
									PreferencesUtils.putString(context, "CurrentCityIcon",currentRegion.region_image);
									PreferencesUtils.putString(context, "CurrentCityId", currentRegion.region_id);
								}
								regionCityTextView.setText(text1);
//								fillRegionListView();
							}
							currentRegionString = regionCityTextView.getText()
									.toString();
						}
					});
			mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
					.location(new LatLng(location.getLatitude(), location
							.getLongitude())));
		} else {
			regionCityTextView.setText(R.string.please_select);
		}
	}

	private void fillRegionListView() {
		// List<Region> regionList =regionDao.getRegionList();
		mRegionAdapter.getDataList().clear();
		if (currentRegion != null) {
			currentCity =PreferencesUtils.getString(context, "CurrentCity");
			String currentCityIcon =PreferencesUtils.getString(context, "CurrentCityIcon");
			currentCityId =PreferencesUtils.getString(context, "CurrentCityId");
			regionCityTextView.setText(text + "/" + currentCity);
			headViewTitle.setText(currentCity);
			ImageLoader.getInstance().displayImage(currentCityIcon,
					headIcon);
			List<Region> sectionAndAreaList = regionDao
					.getSectionAndAreaList(currentCityId);
			if (sectionAndAreaList != null) {
				for (Region region : sectionAndAreaList) {
					region.children = regionDao
							.getSectionAndAreaList(region.region_id);
				}
				mRegionAdapter.getDataList().addAll(sectionAndAreaList);
				mRegionAdapter.notifyDataSetChanged();
			}else{
				//选择了子项的时候
				sectionAndAreaList =regionDao.getSectionAndAreaList(currentRegion.parent_id);
				
			}
		}
	}

	private List<Region> findFatherRegionByChild(Region child) {
		return regionDao.getSectionAndAreaList(child.parent_id);
	}

	private void initCategory() {
		mCategoryAdapter.getDataList().clear();
		List<Category> allCategory = categoryDao.getCategoryList();
		if (allCategory != null) {
			for (Category category : allCategory) {
				if (category.level.equals(PARENT_LEVEL)) {
					category.children = new ArrayList<Category>();
					for (Category child : allCategory) {
						if (category.category_id.equals(child.parent_id)) {
							category.children.add(child);
						}
					}
					mCategoryAdapter.getDataList().add(category);
				}
			}
			mCategoryAdapter.notifyDataSetChanged();
		}
	}

	private Category findFatherCategoryByChild(Category child) {
		List<Category> allCategory = categoryDao.getCategoryList();
		if (allCategory != null) {
			for (Category category : allCategory) {
				if (category.level.equals(PARENT_LEVEL)) {
					category.children = new ArrayList<Category>();
					for (Category childCate : allCategory) {
						if (category.category_id.equals(childCate.parent_id)) {
							category.children.add(childCate);
						}
					}
					if (child.parent_id.equals(category.category_id)) {
						return category;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mGeoCoder.destroy();
	}
}

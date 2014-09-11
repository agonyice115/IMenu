package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.*;
import com.huassit.imenu.android.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigListParser extends
		BaseParser<Map<KeyValuePair, List<? extends BaseModel>>> {

	@Override
	public Map<KeyValuePair, List<? extends BaseModel>> parseData(
			JSONObject jsonObject) throws JSONException {
		Map<KeyValuePair, List<? extends BaseModel>> result = null;
		if (jsonObject.has("data")) {
			JSONObject data = jsonObject.optJSONObject("data");
			if (data.has("config_list")) {
				result = new HashMap<KeyValuePair, List<? extends BaseModel>>(
						18);
				JSONArray configList = data.optJSONArray("config_list");
				List<Region> regionList = new ArrayList<Region>();
				List<Category> categoryList = new ArrayList<Category>();
				List<Environment> environmentList = new ArrayList<Environment>();
				List<Service> serviceList = new ArrayList<Service>();
				List<MenuUnit> menuUnitList = new ArrayList<MenuUnit>();
				List<MenuTaste> menuTasteList = new ArrayList<MenuTaste>();
				List<AreaCode> areaCodeList = new ArrayList<AreaCode>();
				List<Share> shareMenuList = new ArrayList<Share>();
				List<Share> shareMemberList = new ArrayList<Share>();
				List<Share> shareDynamicList = new ArrayList<Share>();
				List<Share> shareStoreList = new ArrayList<Share>();
				List<ClientSkin> clientSkinList = new ArrayList<ClientSkin>();
				List<Feedback> feedbackList = new ArrayList<Feedback>();
				List<AboutInfo> aboutInfoList = new ArrayList<AboutInfo>();
				List<ReturnReason> returnReasonsList = new ArrayList<ReturnReason>();
				List<Version> versionList = new ArrayList<Version>();
				for (int i = 0; i < configList.length(); i++) {
					JSONObject object = configList.optJSONObject(i);
					if (object != null) {
						String key = object.optString("key");
						if (!StringUtils.isBlank(key)) {
							if (object.has("data")
									&& object.optString("data") != null
									&& !StringUtils.isBlank(object
											.optString("data"))) {
								if (key.equals("region")) {
									JSONArray regionArray = object
											.optJSONArray("data");
									for (int j = 0; j < regionArray.length(); j++) {
										regionList.add(new Region()
												.parse(regionArray
														.getJSONObject(j)));
									}
									addResult(result, regionList, object, key);
								} else if (key.equals("category")) {
									JSONArray categoryArray = object
											.optJSONArray("data");
									for (int j = 0; j < categoryArray.length(); j++) {
										categoryList.add(new Category()
												.parse(categoryArray
														.getJSONObject(j)));
									}
									addResult(result, categoryList, object, key);
								} else if (key.equals("environment")) {
									JSONArray environmentArray = object
											.optJSONArray("data");
									for (int j = 0; j < environmentArray
											.length(); j++) {
										environmentList.add(new Environment()
												.parse(environmentArray
														.getJSONObject(j)));
									}
									addResult(result, environmentList, object,
											key);
								} else if (key.equals("service")) {
									JSONArray serviceArray = object
											.optJSONArray("data");
									for (int j = 0; j < serviceArray.length(); j++) {
										serviceList.add(new Service()
												.parse(serviceArray
														.getJSONObject(j)));
									}
									addResult(result, serviceList, object, key);
								} else if (key.equals("menu_unit")) {
									JSONArray menuUnitArray = object
											.optJSONArray("data");
									for (int j = 0; j < menuUnitArray.length(); j++) {
										menuUnitList.add(new MenuUnit()
												.parse(menuUnitArray
														.getJSONObject(j)));
									}
									addResult(result, menuUnitList, object, key);
								} else if (key.equals("menu_taste")) {
									JSONArray menuTasteArray = object
											.optJSONArray("data");
									for (int j = 0; j < menuTasteArray.length(); j++) {
										menuTasteList.add(new MenuTaste()
												.parse(menuTasteArray
														.getJSONObject(j)));
									}
									addResult(result, menuTasteList, object,
											key);
								} else if (key.equals("area_code")) {
									JSONArray areaCodeArray = object
											.optJSONArray("data");
									for (int j = 0; j < areaCodeArray.length(); j++) {
										areaCodeList.add(new AreaCode()
												.parse(areaCodeArray
														.getJSONObject(j)));
									}
									addResult(result, areaCodeList, object, key);
								} else if (key.equals("share_menu")) {
									JSONObject shareMenu = object
											.optJSONObject("data");
									shareMenuList.add(new Share()
											.parse(shareMenu));
									addResult(result, shareMenuList, object,
											key);
								} else if (key.equals("share_store")) {
									JSONObject shareStore = object
											.optJSONObject("data");
									shareStoreList.add(new Share()
											.parse(shareStore));
									addResult(result, shareStoreList, object,
											key);
								} else if (key.equals("share_member")) {
									JSONObject shareMember = object
											.optJSONObject("data");
									shareMemberList.add(new Share()
											.parse(shareMember));
									addResult(result, shareMemberList, object,
											key);
								} else if (key.equals("share_dynamic")) {
									JSONObject shareDynamic = object
											.optJSONObject("data");
									shareDynamicList.add(new Share()
											.parse(shareDynamic));
									addResult(result, shareDynamicList, object,
											key);
								} else if (key.equals("client_skin")) {
									JSONArray clientSkinArray = object
											.optJSONArray("data");
									for (int j = 0; j < clientSkinArray
											.length(); j++) {
										clientSkinList.add(new ClientSkin()
												.parse(clientSkinArray
														.getJSONObject(j)));
									}
									addResult(result, clientSkinList, object,
											key);
								} else if (key.equals("feedback")) {
									JSONArray feedbackArray = object
											.optJSONArray("data");
									for (int j = 0; j < feedbackArray.length(); j++) {
										feedbackList.add(new Feedback()
												.parse(feedbackArray
														.getJSONObject(j)));
									}
									addResult(result, feedbackList, object, key);
								} else if (key.equals("about_info")) {
									JSONArray aboutInfoArray = object
											.optJSONArray("data");
									for (int j = 0; j < aboutInfoArray.length(); j++) {
										aboutInfoList.add(new AboutInfo()
												.parse(aboutInfoArray
														.getJSONObject(j)));
									}
									addResult(result, aboutInfoList, object,
											key);
								} else if (key.equals("return_reason")) {
									JSONArray returnReasonArray = object
											.optJSONArray("data");
									for (int j = 0; j < returnReasonArray
											.length(); j++) {
										returnReasonsList
												.add(new ReturnReason()
														.parse(returnReasonArray
																.getJSONObject(j)));
									}
									addResult(result, returnReasonsList,
											object, key);
								} else if (key.equals("version_android")) {
									Version version = new Version();
									version.url = object.optString("data");
									version.version = object.optString("value");
									versionList.add(version);
									addResult(result, versionList, object, key);
								}
							} else if (key.equals("need_update")) {

								addResult(result, versionList, object, key);
							}
						}
					}
				}
			}
		}
		return result;
	}

	private void addResult(Map<KeyValuePair, List<? extends BaseModel>> result,
			List<? extends BaseModel> list, JSONObject object, String key) {
		KeyValuePair keyValuePair = new KeyValuePair();
		keyValuePair.key = key;
		keyValuePair.value = object.optString("value");
		result.put(keyValuePair, list);
	}
}

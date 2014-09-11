package com.huassit.imenu.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.ui.MenuDetailDynamic;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DynamicGridViewAdapter extends BaseAdapter {

	private Context context;
    private Map<String, Object> map = new HashMap<String, Object>();
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private MemberMenu  memberMenu;
    
    public DynamicGridViewAdapter(Context context) {
    	this.context = context;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
	}
    
    public Map<String, Object> getDataList() {
        return map;

    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int arg0) {
        return map.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder =new ViewHolder();
		
		if(v==null){
			v =inflater.inflate(R.layout.my_dynamic_grid_item, null);
			holder.menuView =(ImageView) v.findViewById(R.id.menu_view);
			holder.menuName =(TextView) v.findViewById(R.id.menu_name);
			v.setTag(holder);
		}else{
			holder =(ViewHolder) v.getTag();
		}
		ArrayList<MemberMenu> memberMenuList =(ArrayList<MemberMenu>) map.get("menu_list");
		memberMenu =memberMenuList.get(position);
		imageLoader.displayImage(memberMenu.image_location+memberMenu.image_name, holder.menuView);
		holder.menuName.setText(memberMenu.menu_name);
		
		holder.menuView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(context,MenuDetailDynamic.class);
				intent.putExtra("memberMenu", memberMenu);
				intent.putExtra("sourceActivity", "Dynamic");
				context.startActivity(intent);
				
			}
		});
		return v;
	}
	
	class ViewHolder {
		ImageView menuView;
		TextView menuName;
	}
}

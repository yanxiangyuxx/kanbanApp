package com.jd.jr.kanbanApp.adapter;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.model.FunctionActivity;
import com.jd.jr.kanbanApp.util.CommonBase;

import java.util.List;

public class HomeFunctionGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private int itemHeight = 0;
	private List<FunctionActivity> functionActivities;
	private LayoutInflater layoutInflater = null;

	public HomeFunctionGridViewAdapter(Context context, List<FunctionActivity> funActivities, int height){
		layoutInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.functionActivities = funActivities;
		this.itemHeight = height;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return functionActivities.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return functionActivities.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(view == null){
			view = layoutInflater.inflate(R.layout.home_activity_functionunit_layout, null);
			//view.setLayoutParams(new ListView.LayoutParams(LayoutParams.FILL_PARENT,(int)mContext.getResources().getDimension(R.dimen.gridview_item)));
			view.setLayoutParams(new ListView.LayoutParams(LayoutParams.FILL_PARENT,itemHeight));
			viewHolder = new ViewHolder();
			viewHolder.funTitle =(TextView)view.findViewById(R.id.function_txt);
			viewHolder.funImg = (ImageView)view.findViewById(R.id.function_img);
			viewHolder.btn_num = (TextView)view.findViewById(R.id.btn_num);
			view.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)view.getTag();
		}
		viewHolder.funImg.setBackgroundResource(functionActivities.get(arg0).imgBg);
		viewHolder.funTitle.setText(functionActivities.get(arg0).title);
//		if(arg0 == 0 && CommonBase.getNewOrderCount() > 0){
//			viewHolder.btn_num.setVisibility(View.VISIBLE);
//			viewHolder.btn_num.setText(CommonBase.getNewOrderCount() + "");
//		}else{
//			viewHolder.btn_num.setVisibility(View.INVISIBLE);
//		}
        viewHolder.btn_num.setVisibility(View.INVISIBLE);
		return view;
	}
	
	class ViewHolder{
		TextView funTitle;
		TextView funActivityClass;
		ImageView funImg;
		TextView btn_num;
	}
}

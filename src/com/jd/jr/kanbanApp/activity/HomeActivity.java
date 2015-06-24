package com.jd.jr.kanbanApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.adapter.HomeFunctionGridViewAdapter;
import com.jd.jr.kanbanApp.model.FunctionActivity;
import com.jd.jr.kanbanApp.util.DPIUtil;
import com.jd.jr.kanbanApp.util.JDLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/5/25.
 */
public class HomeActivity extends Activity {
    private String TAG = "HomeActivity";
    private GridView functionGv;

    private HomeFunctionGridViewAdapter homeFunctionGridViewAdapter;
    private List<FunctionActivity> functionActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        findViewById();
        loadMenuData();
        setListener();
    }


    private void loadMenuData() {
        functionActivities = new ArrayList<FunctionActivity>();
        FunctionActivity functionActivity1 = new FunctionActivity();
        functionActivity1.title = "众筹";
        functionActivity1.imgBg = R.drawable.icon_home_module_order;
        functionActivity1.name = ZCActivity.class;

        functionActivities.add(functionActivity1);


        homeFunctionGridViewAdapter = new HomeFunctionGridViewAdapter(
                HomeActivity.this, functionActivities, loadScreen());
        functionGv.setAdapter(homeFunctionGridViewAdapter);

    }


    public int loadScreen() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int actionHegiht = DPIUtil.dip2px(48);
        int titleHegiht = DPIUtil.dip2px(60);
        int gap_up = DPIUtil.dip2px((float) 2.0);
        int gap_center = DPIUtil.dip2px(1);
        int screenHeight = display.getHeight() * 98 / 100;
        int block = (screenHeight - actionHegiht - titleHegiht - gap_up) / 3;
        int gvItemHeiht = block - 4 * gap_center;
        JDLog.i(TAG, "loadScreen-->>gvItemHeiht:" + gvItemHeiht);
        return gvItemHeiht;
    }

    public void findViewById(){
        functionGv = (GridView) findViewById(R.id.home_function_gv);
    }

    public void setListener() {
        functionGv.setOnItemClickListener(gridViewItemClickListener);

    }


    private AdapterView.OnItemClickListener gridViewItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            // TODO Auto-generated method stub
            if (functionActivities.get(position).name == null) {
                Toast.makeText(HomeActivity.this, "��ʱû��ʵ��", Toast.LENGTH_SHORT)
                        .show();
            }
            else{
                Intent funIntent = new Intent();
                funIntent.setClass(HomeActivity.this,
                        functionActivities.get(position).name);
                startActivity(funIntent);
            }
        }
    };
}

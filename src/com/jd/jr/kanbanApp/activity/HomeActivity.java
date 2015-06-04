package com.jd.jr.kanbanApp.activity;

import android.app.Activity;
import android.os.Bundle;
import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.data.HttpSetting;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/5/25.
 */
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}

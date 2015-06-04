package com.jd.jr.kanbanApp;

import android.app.Application;
import com.jd.jr.kanbanApp.data.RequestManager;
import com.jd.jr.kanbanApp.model.UserInfo;
import com.jd.jr.kanbanApp.util.DPIUtil;

/**
 * Created by Administrator on 2015/5/24.
 */
public class KanbanApp extends Application{

    protected static KanbanApp instance;
    private static UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        userInfo = new UserInfo();

        try {
            DPIUtil.setDensity(this.getResources().getDisplayMetrics().density);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    /**
     * 初始化网络队列
     */
    private void init() {
        RequestManager.init(this);
    }

    public static KanbanApp getInstance() {
        return instance;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }
}

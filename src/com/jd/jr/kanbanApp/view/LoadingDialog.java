
package com.jd.jr.kanbanApp.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jd.jr.kanbanApp.R;

public class LoadingDialog {
    private static final String TAG = "LoadingDialog";
    
    public static void show(Activity activity, String loadingtTxt){
    	//JDLog.v("xxxxx", "show()");
    	View viewLoading = activity.findViewById(R.id.loading_rlayout);
    	if(viewLoading == null){
    		LayoutInflater inflater = LayoutInflater.from(activity);
        	viewLoading = inflater.inflate(R.layout.loading, null);
        	if(loadingtTxt != null){
        		TextView tv = (TextView) viewLoading.findViewById(R.id.loading_txt);
        		tv.setText(loadingtTxt);
        	}
        	viewLoading.setOnTouchListener(new ViewGroup.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        	ViewGroup rootFrameLayout = (ViewGroup) activity.getWindow().peekDecorView();
        	rootFrameLayout.addView(viewLoading);
        	rootFrameLayout.invalidate();
    	}else {
			viewLoading.setVisibility(View.VISIBLE);
		}
    
    }
    
    public static void show(Activity activity) {
    	show(activity, null);
    }

    public static void dismiss(Activity activity) {
        View view = activity.findViewById(R.id.loading_rlayout);
        
        
        if(view == null){
        	//JDLog.v("xxxxx", "null");
        	return;
        }else{
        	//JDLog.v("xxxxx", "!null");
        }
        view.setVisibility(View.GONE);
    }
}

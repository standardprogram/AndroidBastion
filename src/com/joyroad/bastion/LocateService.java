package com.joyroad.bastion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/26.
 */
public class LocateService extends Service {

    private static final String TAG = "LocateService";

    private final int FAST_INTERVAL = 15 * 1000;
    private final int SLOW_INTERVAL = 3 * 60 * 1000;

    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener;


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        SetLocateInterval(FAST_INTERVAL);
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        SetLocateInterval(SLOW_INTERVAL);
        return super.onUnbind(intent);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");


        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");
        super.onCreate();

        mLocationClient = new AMapLocationClient(this);
        mLocationListener = new LocationListener();
        mLocationClient.setLocationListener(mLocationListener);
        SetLocateInterval(SLOW_INTERVAL);
        mLocationClient.startLocation();
    }

    private class LocationListener implements AMapLocationListener{
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息

                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("lat", aMapLocation.getLatitude());
                            obj.put("lng", aMapLocation.getLatitude());

                            Log.d(TAG, obj.toString());
                            UnityPlayer.UnitySendMessage("GameManager", "OnLocationUpdate", obj.toString());
                        }
                        catch (JSONException e) {

                        }
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
    }

    private void SetLocateInterval(int interval) {
        Log.d(TAG, "SetLocateInterval:"+Integer.toString(interval));

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(false);
        mLocationOption.setInterval(interval);

        mLocationClient.setLocationOption(mLocationOption);
    }
}

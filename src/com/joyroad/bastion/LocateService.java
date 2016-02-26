package com.joyroad.bastion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/26.
 */
public class LocateService extends Service {

    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //��λ�ɹ��ص���Ϣ�����������Ϣ
                        aMapLocation.getLocationType();//��ȡ��ǰ��λ�����Դ�������綨λ����������λ���ͱ�
                        aMapLocation.getLatitude();//��ȡγ��
                        aMapLocation.getLongitude();//��ȡ����
                        aMapLocation.getAccuracy();//��ȡ������Ϣ
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);//��λʱ��
                        aMapLocation.getAddress();//��ַ�����option������isNeedAddressΪfalse����û�д˽�������綨λ����л��е�ַ��Ϣ��GPS��λ�����ص�ַ��Ϣ��
                        aMapLocation.getCountry();//������Ϣ
                        aMapLocation.getProvince();//ʡ��Ϣ
                        aMapLocation.getCity();//������Ϣ
                        aMapLocation.getDistrict();//������Ϣ
                        aMapLocation.getStreet();//�ֵ���Ϣ
                        aMapLocation.getStreetNum();//�ֵ����ƺ���Ϣ
                        aMapLocation.getCityCode();//���б���
                        aMapLocation.getAdCode();//��������
                    } else {
                        //��ʾ������ϢErrCode�Ǵ����룬errInfo�Ǵ�����Ϣ������������
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(mLocationListener);


        //����mLocationOption����
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(false);
        mLocationOption.setInterval(2000);

        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mLocationClient.stopLocation();//ֹͣ��λ
        return super.onUnbind(intent);
    }
}

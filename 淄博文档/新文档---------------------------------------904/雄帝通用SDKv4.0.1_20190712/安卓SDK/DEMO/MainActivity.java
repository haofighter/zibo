package com.example.administrator.txsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.wlxsdk.WlxSdk;
import com.tencent.wlxsdk.json.JSONArray;
import com.tencent.wlxsdk.json.JSONException;
import com.tencent.wlxsdk.json.JSONObject;
import com.tencent.wlxsdk.logic.SDKContext;
import com.tencent.wlxsdk.logic.constant.Constant;
import com.tencent.wlxsdk.logic.model.BlackListModel;
import com.tencent.wlxsdk.logic.model.KeyModel;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		//这里的key_list请将下载密钥接口返回的key列表放到这里。
		String key_list ="[{\"key_id\":1,\"key_type\":1,\"key_value\":\"04D74E35A7C6E6540A6FB255AC4C5C414460D246EA595EF3F008352A9BDD028603D471662CDA84DE87C6CBEFBDBCA5BBC82B7244C326392E11\"},{\"key_id\":2,\"key_type\":1,\"key_value\":\"045E7E7E3F6701A6A606BA8F70AB161B07F914348FE27DBB20F4A25B46A6159DF10100F5B5B0F9F669E729154A726B5AF7BDFA589995B1DF0D\"}, {\"key_id\":1,\"key_type\":2,\"key_value\":\"EE32D7E2AC5A46DCB3897B47BFF22336\"}]";
		WlxSdk wlxSdk = new WlxSdk();
		//初始化接口
		int ret = wlxSdk.init(key_list);
		if(0==ret)
		{
			Log.i("wlxSdk init:","初始化密钥列表成功.");
		}
		else
		{
			Log.i("wlxSdk init:","初始化密钥列表失败.");
		}
		Log.i("MainActivity ret=",String.valueOf(ret));
		//验证二维码
		String qrcode = "TXADAP3cXkEXXTkUMTrsQIEUTfr3bYLbzoP33oHTfrbToTTjjXTsLfQDngLfrjnXbYTcUBBQABIAABdjAQCVlSEQdnXKwPOly1SboAaQAATiAAAAAAAAAEAQocpKCUrG4dktT/PUoewGTdoReR0qeo2iBsnxwmNeSyZEqg08CmPTeTmMexmPJ1JlhVOYpGUQQFAFysD0S8AAB+AAAAACnFYOE=";
		int pay_fee = 100;
		byte scene = 1;
		byte scantype = 1;
		String pos_id = "1234567";
		String pos_trx_id = "123456789";
		int vret = wlxSdk.verify(qrcode,pay_fee,scene,scantype,pos_id,pos_trx_id);
		Log.i("wlxSdk verify ret=",String.valueOf(vret));
		if(0==vret)
		{
			String record = wlxSdk.get_record();
			Log.i("wlxSdk record=",record);
			String biz_data_hex = wlxSdk.get_biz_data_hex();
			Log.i("wlxSdk biz_data_hex=",biz_data_hex);
			String card_id = wlxSdk.get_card_id();
			Log.i("wlxSdk card_id=",card_id);
			int biz_data_len = wlxSdk.get_biz_data_len();
			Log.i("wlxSdk biz_data_len=",String.valueOf(biz_data_len));
			int record_len = wlxSdk.get_record_len();
			Log.i("wlxSdk record_len=",String.valueOf(record_len));
			int max_pay_fee = wlxSdk.get_max_pay_fee();
			Log.i("wlxSdk max_pay_fee=",String.valueOf(max_pay_fee));
			int ykt_id = wlxSdk.get_ykt_id();
			Log.i("wlxSdk ykt_id=",Integer.toHexString(ykt_id));
		}
		else
		{
			//保存错误码和日志以便于定位问题
		}
        

    }
}

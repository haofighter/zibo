package com.tencent.ccmsdk.logic;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.tencent.ccmsdk.crypto.Base64;
import com.tencent.ccmsdk.json.JSONArray;
import com.tencent.ccmsdk.json.JSONException;
import com.tencent.ccmsdk.json.JSONObject;
import com.tencent.ccmsdk.logic.constant.Constant;
import com.tencent.ccmsdk.logic.entity.ChargeData;
import com.tencent.ccmsdk.logic.entity.MetroTripInfo;
import com.tencent.ccmsdk.logic.entity.OrderPayInfo;
import com.tencent.ccmsdk.logic.entity.RegInfo;
import com.tencent.ccmsdk.logic.model.BlackListModel;
import com.tencent.ccmsdk.logic.model.BlackListModel.BlackListResult;
import com.tencent.ccmsdk.logic.model.CardIDQueryModel;
import com.tencent.ccmsdk.logic.model.CardIDQueryModel.QueryResult;
import com.tencent.ccmsdk.logic.model.FundBillListModel;
import com.tencent.ccmsdk.logic.model.FundBillListModel.FundBillsResult;
import com.tencent.ccmsdk.logic.model.KeyModel;
import com.tencent.ccmsdk.logic.model.KeyModel.KeyResult;
import com.tencent.ccmsdk.logic.model.MacRootKeyModel;
import com.tencent.ccmsdk.logic.model.MacRootKeyModel.MacRootKeyResult;
import com.tencent.ccmsdk.logic.model.MetroTripInfoPushModel;
import com.tencent.ccmsdk.logic.model.MetroTripInfoPushModel.TripInfoPushResult;
import com.tencent.ccmsdk.logic.model.OrderBillListModel;
import com.tencent.ccmsdk.logic.model.OrderBillListModel.OrderBillsResult;
import com.tencent.ccmsdk.logic.model.OrderPayQueryModel;
import com.tencent.ccmsdk.logic.model.OrderPayQueryModel.OrderPayQueryResult;
import com.tencent.ccmsdk.logic.model.OrderRefundApplyModel;
import com.tencent.ccmsdk.logic.model.OrderRefundApplyModel.OrderRefundApplyResult;
import com.tencent.ccmsdk.logic.model.OrderRefundQueryModel;
import com.tencent.ccmsdk.logic.model.OrderRefundQueryModel.OrderRefundQueryResult;
import com.tencent.ccmsdk.logic.model.PubKeyModel;
import com.tencent.ccmsdk.logic.model.PubKeyModel.PubKeyResult;
import com.tencent.ccmsdk.logic.model.RegNotifyModel;
import com.tencent.ccmsdk.logic.model.RegNotifyModel.RegNotifyResult;
import com.tencent.ccmsdk.logic.model.ReportOrderPayModel;
import com.tencent.ccmsdk.logic.model.ReportOrderPayModel.OrderPayApplyResult;
import com.tencent.ccmsdk.logic.utils.NetUtils;

/**
 * Created by colingzwang on 2018-09-11 21:14. Email: 287712709@qq.com 主要提供对外的接口
 */

public class CcmBackendSDK {


	// 测试
	public static void main(String args[]) {

		Constant.DEBUG_TAG = 0;
		Constant.SECURE_TAG = 1;
		// SDK初始化
		CcmBackendSDK ccmBackendSDK = new CcmBackendSDK("20000176",
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/Vy69K4ELwLL/wdhtLpff54I1\n"
						+ "EOdzqNxRNy3BY8DFKuKDCS7AZUFUvPCKxbnUkAVJguft4eHscZQgvN3ipNW81+vu\n"
						+ "iI2cRftmZpUbEti8g5wHZaNfqxQmTwwy5/xFuNN2U4eeiV3wo1nJ58tW4vsgrowR\n"
						+ "EO5y8BoCeMNFAtaZdQIDAQAB",
				"MIICXAIBAAKBgQCwg4s1heYuVHdeV9qsgXMJHO5XVlTxFVpxz/DgZlr/Wo/p2tzU\n"
						+ "k3aJ33nVA0rAiwOWjyvNM5J1NbpKZRAV1qOPtsU0sRLKxMt+YrZF7RRoi5rJrZ8Z\n"
						+ "LxPGdmLkbQXn7Qp3Jtn7iBPZkikiZc7w5yCNf/Xa54UbHFCUv5NGLXW+jwIDAQAB\n"
						+ "AoGAOXkra6sEjtNL5rkeZ4rixPXZmTBwCeuU/nfhi39oY7q+Hzv3KXQ2ZaARUE15\n"
						+ "GoZpDa3iajc/mdB7rtuHSEUSDh+6EtZCOFR8PZkOqunb7tCkvyjC6w0q0+jG7s5g\n"
						+ "pEaySgmuKE8+kIZ0Re2as53zo2qfxzLkhP13XmhWro3tgkECQQDd//J/kilrIxfo\n"
						+ "inmSFdtd8VFqUNEe8y1/TYIjZInIrhiDPNB/eWFNbt8R4qCeX15GhZ6ZUlOP0oEK\n"
						+ "qypRkgovAkEAy4w0+xu0vZiu+GwgIob1izHEl7DA6vmCz9HiA5NY/cjGrQRgdmx6\n"
						+ "DkbG7zC3fzeTnE1EWOckF6a2MeylGF1ZoQJALl+azl8/26t3ARJ8FrIOIu+X7Dd5\n"
						+ "l5eAt4j/WFlWFt+XK0L24sn+M2innFrU5oBRdzXOTYTPA8obPplGu8df7QJBAKG3\n"
						+ "2pwrbhU4ysM6/OkRuuKFfwFaAFxwMrs0sNJQbmLr8tWh5ZYRJ4RSPVnqpc+gc1m6\n"
						+ "lfPgaO+Vl6ngr2bFPCECQFm0nyEMWKzaiTxfMf4lc0PsM3v6wNnAQAONF9AWN9Gl\n"
						+ "IaydgmmZz+ZR65lQgFykghWf0/P+nzLdJ2fkqo8hoUI=");

		JSONArray array = new JSONArray();
		JSONObject jsonObject = null;

		// *********************密钥列表下载接口*********************
		KeyResult keyResult = ccmBackendSDK.downloadKey();
		if (keyResult.retcode != 0) {
			Constant.LogDebug("retcode:" + keyResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + keyResult.retmsg + "\n");

			Constant.LogDebug("密钥列表下载接口失败\n");
		} else {
			for (int i = 0; i < keyResult.keylist.size(); i++) {
				Constant.LogDebug(Integer.toString(keyResult.keylist.get(i).key_id) + "\n");
				Constant.LogDebug(Integer.toString(keyResult.keylist.get(i).key_type) + "\n");
				Constant.LogDebug(keyResult.keylist.get(i).key_value + "\n");
				jsonObject = new JSONObject();
				try {
					jsonObject.put("key_id", keyResult.keylist.get(i).key_id);
					jsonObject.put("key_type", keyResult.keylist.get(i).key_type);
					jsonObject.put("key_value", keyResult.keylist.get(i).key_value);
				} catch (JSONException e) {
					Constant.LogDebug("toJson expcetion: " + e.getMessage() + "\n");
				}
				array.put(jsonObject);
			}
			Constant.LogDebug("密钥列表下载成功\n");
			Constant.LogDebug("array:" + array.toString() + "\n");
		}

		// *********************黑名单接口测试*********************
		// Date begin_time = new Date();
		// begin_time.setTime(System.currentTimeMillis());
		// Date end_time = new Date();
		// end_time.setTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
		// BlackListResult blackListResult = ccmBackendSDK.downloadBlackList(0, 300,
		// begin_time, end_time, "");
		// if (blackListResult.retcode == 0) {
		// for (int i = 0; i < blackListResult.blacklist.size(); i++) {
		// Constant.LogDebug("open_id:" + blackListResult.blacklist.get(i).open_id + "
		// ");
		// Constant.LogDebug("exprie_time:" +
		// blackListResult.blacklist.get(i).exprie_time + " ");
		// Constant.LogDebug("ykt_card_id:" +
		// blackListResult.blacklist.get(i).ykt_card_id + "\n");
		// }
		// } else {
		// Constant.LogDebug("retcode:" + blackListResult.retcode + "\n");
		// Constant.LogDebug("retmsg:" + blackListResult.retmsg + "\n");
		// }

		// *********************申请扣款接口*********************

		OrderPayInfo order = new OrderPayInfo();
		order.ykt_trx_id = "45120385_20190611111307_8750"; // 商户订单号
		order.card_id = "3010DF1A10537621"; // 用户卡号
		order.total_fee = 1; // 商户名义收款金额
		order.collect_fee = 1; // 商户实际收款金额
		order.discount_fee = 0; // 商户减免金额
		// order.compensation_fee = 0; // 赔给商户的金额
		order.ykt_trx_desc = "公交-12路"; // 商户订单描述 微信订单展示的订单描述信息
		order.exp_type = 0; // 订单异常类型
		order.charge_type = "0"; // 计费类型
		order.city_id = "530100"; // 城市号

		ChargeData in_charge_data = new ChargeData(); // 计费数据
		in_charge_data.record_type = 1; // 计费类型
		in_charge_data.qrcode_type = "1"; // 扫码数据类型
		in_charge_data.record_time = 1560222787; // 记录时间，Unix时间戳
		in_charge_data.record_data = "AAEBAkSHVvPEsZI4NIUiZ37I54Ajh1ujSFLtpl1qIEMnkHTWwoUdLek2Rmh5pOtjr4ZLd7Eh0BKSqBuKZj9ydaS8O/o+MwxwSJ5pmtAFX4kK/OUb+k+pmbIin10IzOJ9XKRBUQ=="; // 扫码数据
		// in_charge_data.station_id = "10"; //站点编号
		// in_charge_data.station_name = "1站"; //站点名
		in_charge_data.terminal_id = "4512038500149723B263"; // 机具或闸机id
		in_charge_data.scan_no = "415"; // 扫码流水号
		// in_charge_data.trip_num = "12345678"; //行程id
		// in_charge_data.line_no = "12345678"; //线路号
		// in_charge_data.line_name = "线路名"; //线路名
		// in_charge_data.vehicle_no = "12345678"; //车辆编号
		// in_charge_data.plate_no = "12345678"; //车牌号
		// in_charge_data.driver_id = "12345678"; //司机编号
		// in_charge_data.longitude = "12345678"; //经度
		// in_charge_data.latitude = "12345678"; //纬度

		// ChargeData out_charge_data = new ChargeData(); //计费数据
		// out_charge_data.record_type = 2; //计费类型
		// out_charge_data.qrcode_type = "1"; //扫码数据类型
		// out_charge_data.record_time = 1553756293; //记录时间，Unix时间戳
		// out_charge_data.record_data =
		// "AAEBAh7lo76tiFsn8IqTHNtFWzC5Vu7Fz6Pz7yekmAMOON83LtcWWQ6dTJBN6pkYdLxkBjt/BcRHEdV4gJtLk23QLiR/XvXaMIaSx25MzY+tkRqmGM9YBDErkL/pRoyVr9PcEg==";
		// //扫码数据
		// out_charge_data.station_id = "10"; //站点编号
		// out_charge_data.station_name = "1站"; //站点名
		// out_charge_data.terminal_id = "323431303139313033"; //机具或闸机id
		// out_charge_data.scan_no = "123456798"; //扫码流水号
		// out_charge_data.trip_num = "12345678"; //行程id
		// out_charge_data.line_no = "12345678"; //线路号
		// out_charge_data.line_name = "线路名"; //线路名
		// out_charge_data.vehicle_no = "12345678"; //车辆编号
		// out_charge_data.plate_no = "12345678"; //车牌号
		// out_charge_data.driver_id = "12345678"; //司机编号
		// out_charge_data.longitude = "12345678"; //经度
		// out_charge_data.latitude = "12345678"; //纬度

		order.charge_data.add(in_charge_data); // 进站计费数据
		// order.charge_data.add(out_charge_data);

		OrderPayApplyResult orderPayApplyResult = ccmBackendSDK.orderPayApply(order);
		if (orderPayApplyResult.retcode != 0) // retcode非0保存日志以便于定位问题
		{
			Constant.LogDebug("retcode:" + orderPayApplyResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderPayApplyResult.retmsg + "\n");
			Constant.LogDebug("申请扣款接口失败\n");
		} else {
			Constant.LogDebug("retcode:" + orderPayApplyResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderPayApplyResult.retmsg + "\n");
			Constant.LogDebug("ykt_trx_id:" + orderPayApplyResult.ykt_trx_id + "\n");
			Constant.LogDebug("order_id:" + orderPayApplyResult.order_id + "\n");
			Constant.LogDebug("status:" + orderPayApplyResult.status + "\n");
			if (orderPayApplyResult.status == 0 || orderPayApplyResult.status == 11) {
				Constant.LogDebug("申请扣款接口成功\n");
			} else {
				// status非0保存日志以便于定位问题
			}
		}

		// *********************查询扣款单接口*********************
		String ykt_trx_id = "11551420422514265030122020190301"; // 商户订单号
		ykt_trx_id = "99999999_20190612093001_885";
		String order_id = "12312312312";
		OrderPayQueryResult orderQueryResult = ccmBackendSDK.orderPayQuery(ykt_trx_id, null);
		if (orderQueryResult.retcode != 0) {
			Constant.LogDebug("retcode:" + orderQueryResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderQueryResult.retmsg + "\n");
			Constant.LogDebug("查询扣款单接口失败\n");
		} else {
			Constant.LogDebug("retcode:" + orderQueryResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderQueryResult.retmsg + "\n");
			if (orderQueryResult.order_exist == 0) {
				Constant.LogDebug("查询扣款单接口成功,但是订单不存在\n");
			} else {
				Constant.LogDebug("ykt_trx_id:" + orderQueryResult.ykt_trx_id + "\n");
				Constant.LogDebug("order_id:" + orderQueryResult.order_id + "\n");
				Constant.LogDebug("order_time:" + orderQueryResult.order_time + "\n");
				Constant.LogDebug("card_id:" + orderQueryResult.card_id + "\n");
				Constant.LogDebug("city_id:" + orderQueryResult.city_id + "\n");
				Constant.LogDebug("total_fee:" + orderQueryResult.total_fee + "\n");
				Constant.LogDebug("collect_fee:" + orderQueryResult.collect_fee + "\n");
				Constant.LogDebug("discount_fee:" + orderQueryResult.discount_fee + "\n");
				Constant.LogDebug("exp_type:" + orderQueryResult.exp_type + "\n");
				Constant.LogDebug("charge_type:" + orderQueryResult.charge_type + "\n");
				Constant.LogDebug("status:" + orderQueryResult.status + "\n");
			}
			Constant.LogDebug("查询扣款单接口成功\n");
		}

		// *********************申请退款接口*********************
		OrderPayInfo order2 = new OrderPayInfo();
		order2.ykt_refund_id = "2123456745";
		order2.ykt_trx_id = "01234567892168"; // 商户订单号
		order2.card_id = "301030C7F720A767"; // 用户卡号
		order2.total_fee = 100;
		order2.refund_fee = 100;
		order2.refund_desc = "test";
		OrderRefundApplyResult orderRefundApplyResult = ccmBackendSDK.orderRefundApply(order2);
		if (orderRefundApplyResult.retcode != 0) {
			Constant.LogDebug("retcode:" + orderRefundApplyResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderRefundApplyResult.retmsg + "\n");
			Constant.LogDebug("退款接口失败\n");
		} else {
			Constant.LogDebug("retcode:" + orderRefundApplyResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderRefundApplyResult.retmsg + "\n");
		}

		// *********************查询退款单接口*********************

		String ykt_refund_id = "2123456744";
		String ykt_trx_id1 = "01234567892168"; // 商户订单号
		OrderRefundQueryResult orderRefundQueryResult = ccmBackendSDK.orderRefundQueryResult(ykt_trx_id1,
				ykt_refund_id);
		if (orderRefundQueryResult.retcode != 0) {
			Constant.LogDebug("retcode:" + orderRefundQueryResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderRefundQueryResult.retmsg + "\n");
			Constant.LogDebug("查询退款接口失败\n");
		} else {
			Constant.LogDebug("retcode:" + orderRefundQueryResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + orderRefundQueryResult.retmsg + "\n");
			if (orderRefundQueryResult.order_exist == 0) {
				Constant.LogDebug("查询退款接口成功,但是订单不存在\n");
			} else {
				Constant.LogDebug("ykt_trx_id:" + orderRefundQueryResult.ykt_trx_id + "\n");
				Constant.LogDebug("total_fee:" + orderRefundQueryResult.total_fee + "\n");
				Constant.LogDebug("collect_fee:" + orderRefundQueryResult.collect_fee + "\n");
				Constant.LogDebug("refund_fee:" + orderRefundQueryResult.refund_fee + "\n");
				Constant.LogDebug("refund_status:" + orderRefundQueryResult.refund_status + "\n");
				Constant.LogDebug("err_desc:" + orderRefundQueryResult.err_desc + "\n");
			}
			Constant.LogDebug("查询退款接口成功\n");
		}

		// *********************下载对账单接口*********************
		// Date date1 = new Date();
		// date1.setTime(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000);
		// Constant.LogDebug("date1:" + date1 + "\n");
		// SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
		// Constant.LogDebug(simpleDateFormat1.format(date1) + "\n");
		// String version = "3.0";
		// String filePath = "D:\\\\workspace\\\\orderBillsFile_0911.csv"; // 出账单文件路径
		// OrderBillsResult orderBillsResult = ccmBackendSDK.downloadOrderBills(version,
		// date1,
		// "D:\\workspace\\orderBillsFile_0911.csv");
		// if (orderBillsResult.retcode != 0) {
		// Constant.LogDebug("retcode:" + orderBillsResult.retcode + "\n");
		// Constant.LogDebug("retmsg:" + orderBillsResult.retmsg + "\n");
		// Constant.LogDebug("下载对账单接口失败\n");
		// }
		//
		// // *********************下载资金账单接口*********************
		// Date date2 = new Date();
		// date2.setTime(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000);
		// FundBillsResult fundBillsResult = ccmBackendSDK.downloadFundBills(version,
		// date2,
		// "D:\\workspace\\fundBillsFile_0912.csv");
		// if (fundBillsResult.retcode != 0) {
		// Constant.LogDebug("retcode:" + fundBillsResult.retcode + "\n");
		// Constant.LogDebug("retmsg:" + fundBillsResult.retmsg + "\n");
		// Constant.LogDebug("下载资金账单接口失败\n");
		// }

		/*----------手机号查询卡id--线上环境接口------*/
		// QueryResult queryResult = ccmBackendSDK.cardIDQuery("13686816615");
		// if (queryResult.retcode == 0) {
		// Constant.LogDebug("queryResult.cardid" + queryResult.cardid);
		// } else {
		// Constant.LogDebug("retcode:" + queryResult.retcode + "\n");
		// Constant.LogDebug("retmsg:" + queryResult.retmsg + "\n");
		// Constant.LogDebug("手机号查询卡ID失败\n");
		// }

		// *********************补登消息接口*********************
		// ArrayList<RegInfo> regs = new ArrayList<RegInfo>();
		// RegInfo regInfo = new RegInfo();
		// regInfo.ykt_reg_id = "20001400200000201808012050000025"; // 合作方补登流水号，必填
		// regInfo.action_id = "1234567890"; // 行程id,必填
		// regInfo.card_id = "2400F1F491457510"; // 账户id,必填
		// regInfo.line_id = "123"; // 线路id,必填
		// regInfo.line_type = "2"; // 线路类型，必填
		// regInfo.line_name = "oneline"; // 线路名称，必填
		// regInfo.station_name = "city—station"; // 站点名称，必填
		// regInfo.station_id = "5"; // 站点id,必填
		// regInfo.device_id = "123456"; // 机具id,必填
		// regInfo.scan_time = 1533563465; // 扫码时间,必填
		// regInfo.max_fee = 100; // 最大金额,必填
		// regInfo.reg_type = 2; // 补登类型，必填
		// regInfo.expire_time = 1533573465; // 补登超时时间，必填
		// regs.add(regInfo);
		// RegNotifyResult regNotifyResult = ccmBackendSDK.regNotify(regs); // 调用补登接口
		// if (regNotifyResult.retcode != 0) {
		// Constant.LogDebug("retcode:" + regNotifyResult.retcode + "\n");
		// Constant.LogDebug("retmsg:" + regNotifyResult.retmsg + "\n");
		// Constant.LogDebug("result:" + regNotifyResult.result + "\n"); // 返回结果
		// Constant.LogDebug("message:" + regNotifyResult.message + "\n"); // 返回结果消息
		// Constant.LogDebug("reg_id:" + regNotifyResult.reg_id + "\n"); // 腾讯补登订单号
		// Constant.LogDebug("ykt_reg_id:" + regNotifyResult.ykt_reg_id + "\n");
		// // 合作方补登订单号
		// Constant.LogDebug("补登消息接口失败\n");
		// } else {
		// Constant.LogDebug("retcode:" + regNotifyResult.retcode + "\n"); // 返回码
		// Constant.LogDebug("retmsg:" + regNotifyResult.retmsg + "\n"); // 返回消息
		// Constant.LogDebug("result:" + regNotifyResult.result + "\n"); // 返回结果
		// Constant.LogDebug("message:" + regNotifyResult.message + "\n"); // 返回结果消息
		// Constant.LogDebug("reg_id:" + regNotifyResult.reg_id + "\n"); // 腾讯补登订单号
		// Constant.LogDebug("ykt_reg_id:" + regNotifyResult.ykt_reg_id + "\n");
		// // 合作方补登订单号
		// Constant.LogDebug("补登消息接口成功\n");
		// }

		// *********************推送行程数据测试*********************
		MetroTripInfo metro_trip_info = new MetroTripInfo();
		metro_trip_info.card_id = "3010DF1A10537621"; // 账户id(腾讯分配)，必填
		metro_trip_info.ykt_card_id = "20190430183100000001"; // 卡id(地铁分配)，必填
		metro_trip_info.action_id = "1234567890"; // 行程id，用于去重，必填
		metro_trip_info.action_src = "1"; // 进/出站记录类型，1：扫码进/出站 2:客服补进/出站，必填
		metro_trip_info.ticket_seqno = "28766577"; // 行程数据id，非必填
		metro_trip_info.line_id = "2"; // 上次出入站的线路id，非必填
		metro_trip_info.line_name = "地铁线ssss"; // 上次出入站的线路名，非必填
		metro_trip_info.station_id = "123456"; // 上次出入站的站点编号，必填
		metro_trip_info.station_name = "Station A"; // 上次出入站的站点名称，必填
		metro_trip_info.device_id = "1234567890"; // 设备id，非必填
		metro_trip_info.scan_time = "2018-08-08 02:23:34"; // 扫码时间，必填
		metro_trip_info.scan_type = "1"; // 扫码类型，非必填
		metro_trip_info.biz_amount = "200"; // 交易金额，非必填
		metro_trip_info.biz_subject = "1路-城-001设备"; // 格式：线路-车站-设备，非必填
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date3 = new Date();
		date3.setTime(System.currentTimeMillis());
		metro_trip_info.biz_time = simpleDateFormat2.format(date3); // 交易时间，非必填
		metro_trip_info.report_time = "2018-07-27 17:00:34"; // 上报时间，必填
		TripInfoPushResult tripInfoPushResult = ccmBackendSDK.tripInfoPush(metro_trip_info); // 调用推送地铁行程数据接口
		if (tripInfoPushResult.retcode != 0) {
			Constant.LogDebug("retcode:" + tripInfoPushResult.retcode + "\n"); // 返回码
			Constant.LogDebug("retmsg:" + tripInfoPushResult.retmsg + "\n"); // 返回消息
			Constant.LogDebug("推送行程接口失败\n");
		} else {
			Constant.LogDebug("retcode:" + tripInfoPushResult.retcode + "\n");
			Constant.LogDebug("retmsg:" + tripInfoPushResult.retmsg + "\n");
			Constant.LogDebug("推送行程接口成功\n");
		}

	}
}

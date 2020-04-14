package com.tencent.wlxsdk;

import android.text.TextUtils;
import java.util.Locale;


public class WlxSdk {
	static {
		System.loadLibrary("txccm_pos_sdk");
	}

	//进出站标识
	private static final int ENTER = 1;
	private static final int EXIT = 2;

	//扫码记录
	private byte[] record = null;
	private byte[] card_id = null;
	private int ykt_id = -1;
	private int max_pay_fee = -1;
	private int biz_data_len = 0;
	private int record_len = 0;

	private byte[] biz_data = null;

	private native int init_txccm_pos_sdk(String key_list);
	private native int txccm_verify_qrcode(String qrcode, int payfee, byte scene,  byte scantype, String pos_id, String pos_trx_id);

	public int get_biz_data_len() {
		return biz_data_len;
	}
	public int get_record_len() {
		return record_len;
	}

	public int get_ykt_id() {
		return ykt_id;
	}

	public String get_card_id() {
		return card_id != null ? new String(card_id) : "";
	}

	public String get_biz_data() {
		return biz_data != null ? new String(biz_data) : "";
	}

	public String get_biz_data_hex(){
		return biz_data != null ? bytesToHexString(biz_data) : "";
	}
	public int get_max_pay_fee() {
		return max_pay_fee ;
	}

	public String get_record() {
		return record != null ? new String(record) : "";
	}

	public int init(String key_list){
		return init_txccm_pos_sdk(key_list);
	}
	/**
	 * @param qrcode  二维码
	 * @param payfee  实际扣款金额（扣除优惠），单位是分
	 * @param scene 场景：1公交，2地铁
	 * @param scantype  扫码类型：1进站 2出站
	 * @param pos_id 机具ID
	 * @param pos_trx_id  机具流水号
	 * @return
	 */
	public int verify(String qrcode, int payfee, byte scene,  byte scantype, String pos_id, String pos_trx_id){
		if (TextUtils.isEmpty(qrcode)  || payfee < 0 || (scantype != ENTER && scantype != EXIT) ) {
			return -1;
		}
		return txccm_verify_qrcode( qrcode,  payfee,  scene,   scantype,  pos_id,  pos_trx_id);
	}
	/**
	 * byte数组转十六进制字符串，大端
	 * @param input
	 * @return
	 */
	public static String bytesToHexString(byte[] input) {
		if (input == null) {
			return "";
		}

		return bytesToHexString(input, 0, input.length);
	}

	/**
	 * byte数组转十六进制字符串，大端
	 * @param input
	 * @param start
	 * @param n
	 * @return
	 */
	public static String bytesToHexString(byte[] input, int start, int n) {
		if (input == null || start < 0 || n < 0 || input.length < start + n) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			sb.append(String.format(Locale.getDefault(), "%02X", input[i]));
		}

		return sb.toString();
	}
}

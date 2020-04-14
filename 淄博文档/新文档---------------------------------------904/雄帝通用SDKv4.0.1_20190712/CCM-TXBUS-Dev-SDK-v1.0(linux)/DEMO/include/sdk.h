#ifndef _WLX_SDK_H_
#define _WLX_SDK_H_
  
#ifdef  __cplusplus
extern "C" {
#endif

#include "cJSON.h"

/*
成功
*/
#define EC_SUCCESS 0

/*
二维码格式错误
*/
#define EC_FORMAT -10000

/*
卡证书公钥错误
*/
#define EC_CARD_PUBLIC_KEY -10001

/*
卡证书验签失败
*/
#define EC_CARD_CERT -10002

/*
卡证书用户公钥错误
*/
#define EC_USER_PUBLIC_KEY -10003

/*
二维码验签错误
*/
#define EC_USER_SIGN -10004

/*
卡证书过期
*/
#define EC_CARD_CERT_TIME -10005

/*
二维码过期
*/
#define EC_CODE_TIME -10006

/*
超过最大金额
*/
#define EC_FEE -10007

#define EC_BALANCE -10008

#define EC_OPEN_ID -10009

//参数错误
#define EC_PARAM_ERR -10010
//内存申请错误
#define EC_MEM_ERR -10011
//卡证书签名算法不支持
#define EC_CARD_CERT_SIGN_ALG_NOT_SUPPORT -10012
//加密的mac根密钥解密失败
#define EC_MAC_ROOT_KEY_DECRYPT_ERR -10013
//mac校验失败
#define EC_MAC_SIGN_ERR -10014
//二维码签名算法不支持
#define EC_QRCODE_SIGN_ALG_NOT_SUPPORT -10015
//扫码记录加密失败
#define EC_SCAN_RECORD_ECRYPT_ERR -10016
//扫码记录编码失败
#define EC_SCAN_RECORD_ECODE_ERR -10017
//闸机扫码时间与二维码生成时间不匹配
#define EC_SACN_TIME -10018

//入参的json结构体有误
#define EC_JSON_IN_PARAM -10020
//输入的密钥列表为空
#define EC_KEYLIST_NULL	-10021
//输入的密钥列表长度错误
#define EC_KEYLIST_LENGTH	-10022
//密钥列表未进行初始化
#define EC_KEYLIST_INIT	-10023
//密钥列表格式错误
#define EC_KEYLIST_FORMAT	-10024
//密钥列表个数错误
#define EC_KEYLIST_NUM	-10025
//输入的密钥列表中类型错误
#define EC_KEYTYPE_ERR	-10026
//输入的公钥列表指针为空
#define EC_PUBKEYLIST_NULL	-10027
//输入的MAC列表指针为空
#define EC_MACKEYLIST_NULL	-10028
//输入的公钥个数是否和公钥列表中个数不一致
#define EC_PUBKEYNUM_ERR	-10029
//输入的MAC个数是否和MAC列表中个数不一致
#define EC_MACKEYNUM_ERR	-10030
//输入的公钥列表中的字段名称错误
#define EC_PUBKEYNAME_ERR	-10031
//输入的MAC列表中的字段名称错误
#define EC_MACKEYNAME_ERR	-10032
//公钥列表中无与二维码公钥id匹配的公钥
#define EC_PUBKEYID_ERR	-10033
//MAC列表中无与二维码MACid匹配的MAC
#define EC_MACKEYID_ERR	-10034
//同个二维码重复扫码错误
#define EC_DUPLICATION_QRCODE -10035
/*
其它错误
*/
#define EC_FAIL -20000

typedef struct
{
	//二维码数据
	char *qrcode;
	//二维码长度
	int qrcode_len;
	//地铁闸机信息
	char *pos_param;
}TXCCM_REQUEST;

typedef struct
{
	/*用户卡ID*/
	char card_id[32];

	//以下数据用来返回给调用方，用于MAC计算的数据来源
	//yktid 8位数字
	unsigned int ykt_id;

	//单笔限额
	unsigned int max_pay_fee;

	//合作方数据
	unsigned char biz_data[256];

	//合作方数据长度
	int biz_data_len;

	/*扫码记录*/
	char* record;

	/*扫码记录长度*/
	int record_len;

}TXCCM_RESPONSE;

/*
初始化内存池 只在自己分配内存，不使用系统malloc的场景，如ARM环境才需要调用
*/
void  SDKMemPoolInit(unsigned char *ptr, unsigned int size);

/*
分配扫码记录内存
IN: void
return：输出结构体
*/
TXCCM_RESPONSE * SDK_QRCODE_OUT_New();

/*
释放扫码记录内存
IN: qrcode_out_params 扫码记录所在的输出结构体
*/
void SDK_QRCODE_OUT_Free(TXCCM_RESPONSE *txccm_response);

/*
SDK初始化接口,用于初始化SDK
IN: key_list 密钥列表
return: EC_SUCCESS表示成功，其它表示对应的错误码
*/
int init_txccm_pos_sdk(const char* key_list);

/*
验证二维码
IN:  qrcode二维码
IN:  qrcode_len 二维码长度
IN:  QRCODE_IN_PARAMS 二维码附带的输入参数
OUT: QRCODE_OUT_PARAMS 二维码附带的输出参数
return: EC_SUCCESS表示成功，其它表示对应的错误码
*/
int txccm_verify_qrcode(TXCCM_REQUEST* txccm_request, TXCCM_RESPONSE* txccm_response);



#ifdef __cplusplus
} /* end of extern "C" */
#endif


#endif
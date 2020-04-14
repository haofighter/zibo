#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <zlib.h>

#include "pos_crypto.h"
//#include "demo.h"
#include "alipay_sdk_client.h"
#include "alipay_sdk_bus.h"

#define TEST_ALIPAY_PUBLIC_KEY  "028B90D7C281AD21B4BA9492C522E39B655890CC41420EB8E8826B28C6DF2D467A"
#define TEST_MOT_TRIPLE_SM2_KEY "03F8ECFA98612ECB5CF9C0EBCAA8C9C67714F6EE948CFF7226EF7E39000542930F"
//#define TEST_MOT_TRIPLE_SM2_KEY "036D955C7FE3271359E7963CC7E06AF4E3A6FCB90F4A436ADD02DA9FC0A9DB7857"

#define TEST_MOT_DOUBLE_SM2_KEY "024B32DCE5E387EA6B66063E0CD323F7B1DA9FCFCEB67F89610EBDAF2DA43FFF47"

#define QRCODE_HEX_DATA "02010057323038383230323931343938303737355E1AF1510A0D0A0D000000000000000044454D4F03BEDDEA3AAA38BABEA523A6727F066248FCCBEE59CA00165A54455354303030311032303838323032393134393830373735004730450221008A6E466CA550B8D0C0E2C81DC1C517FD0F698277383FDDD78A51C31B0419EA9802200B2008518C7804D09D348D1BC617D717EA3FDCC94B9632AED971E985C63AA66C045C925581373035021900B3AC8382AE7E61993E61F6FD3DAE725E52A284BBF1E9DC01021867CAD1BD062FA574FE8005C30478E6DCB3527282EB37D3C2"
#define AMOUNT_CENT 200

//#######################################
int _TEST_CASE_AUTO_INCR_ = 0;
void (*test_func_arr[1024])();
#define TINIT() do{ printf("============== Testcase List ==============\n");}while(0)
#define TLIST(func,desc) do {printf("[%d]["#func"]["#desc"]\n",_TEST_CASE_AUTO_INCR_); test_func_arr[_TEST_CASE_AUTO_INCR_]=func;++_TEST_CASE_AUTO_INCR_;}while(0)
#define TRUN(idx) do{if(idx >=0 && idx < _TEST_CASE_AUTO_INCR_) {test_func_arr[idx]();printf("\n");}else {printf("[ERROR]wrong testcase index\n");}}while(0)

typedef struct{
    char ctl_chr;
    int ctl_num;
    int case_id;
} Tcmd;

void tlog(const char * log);
int parse_single(const char *str,Tcmd *cmd);
int parse_cmd_str(char *str,Tcmd *cmd);
int parse_args(int argc,char **argv,Tcmd *cmd);
void execute(Tcmd cmd);
//#######################################

void print_help_info(){
    printf("----------------------------------------------------------\n");
    printf("Usage:\n");
    printf("  -h      print this help message.\n");
    printf("  -T[num] run testcase index of [num]. i.e. -T1 -T2 \n");
    printf("  -L[cnt] loop [cnt] time. i.e. -L3 -L5\n");
    printf("  -q      exit.\n");
    printf("----------------------------------------------------------\n");
}

void test_ctrl_open_log(void) {
    printf("begin ctrl_open\n");
    int rv = alipay_iot_control(ALIPAY_IOT_SET_STATUS, 1);
    printf("ctrl_open rv is : %d\n", rv);
    printf("end ctrl_open\n");
}

void test_ctrl_close_log(void) {
    printf("begin ctrl_close\n");
    int rv = alipay_iot_control(ALIPAY_IOT_SET_STATUS, 0);
    printf("ctrl_close rv is : %d\n", rv);
    printf("end ctrl_close\n");
}

void test_ctrl_get_status_log(void) {
    printf("begin get_status_log\n");
    int status = 0;
    int rv = alipay_iot_control(ALIPAY_IOT_GET_STATUS, &status);
    printf("ctrl_close rv is : %d status is : %d\n", rv, status);
    printf("end get_status_log\n");
}

void test_ctrl_set_fetch_log(void) {
    printf("begin set_fetch_log\n");
    int status = 0;

    const char* cmdJSON = "{\"cmdname\":\"fecth_log\",\"startTime\":\"2019-02-28 10:00:00\",\"endTime\":\"2019-02-29 10:00:00\"}";

    int rv = alipay_iot_control(ALIPAY_IOT_EXE_COMMAND, cmdJSON);
    printf("set_fetch_log rv is : %d\n", rv);
    printf("end set_fetch_log\n");
}

void test_set_config(void) {
    printf("begin set_config\n");
    aliot_dict* dict = aliot_dict_new();
    aliot_dict_set(dict, "ticket_price", "2.00");
    //aliot_dict_set(dict, "actual_price", "2.00");
    //aliot_dict_set(dict, "vehicle_no", "0023");
    //aliot_dict_set(dict, "line", "192路");
    //aliot_dict_set(dict, "company", "北京市交通公司");
    //aliot_dict_set(dict, "city", "北京");
    //aliot_dict_set(dict, "driver_id", "1234567");
    aliot_dict_set(dict, "device_id", "12345678"); //厂商保证序列号唯一
    aliot_dict_set(dict, "supplier_id", "ABCDEFG");//供应商ID 向支付宝申请
    aliot_dict_set(dict, "item_id", "2390392329");//设备类型ID 向支付宝申请
    int rv = alipay_iot_set_config(dict);
    printf("set_config rv is : %d\n", rv);
    aliot_dict_del(dict);
    printf("end set_config\n");
}

void test_store_data(void) {
    printf("begin store_data\n");
    int status = 0;

    alipay_iot_extra_params params;
    params.product_id = "test";
    params.biz_type = "hello";
    params.sub_type = "world";

    aliot_dict* dict = aliot_dict_new();
    aliot_dict_set_int64(dict, "current_time", time(NULL));
    int rv = alipay_iot_data_store(&params, dict);
    printf("store_data rv is : %d\n", rv);
    aliot_dict_del(dict);
    printf("end store_data\n");
}


char print_buf[1024] = {0};

static int demo_init();

int hex_string_to_bytes_a(
	char* hex_string, 
	int hex_string_len, 
	unsigned char* bytes, 
	int bytes_len);
/**
char* bytes_to_hex_string(
	char* print_buf, 
	int print_buf_len, 
	const unsigned char* bytes, 
	int len);
**/
unsigned char hex_of_char(char c);

static void print_info_response(INFO_RESPONSE *response);
/**
 * 验证二维码例程
 *
 * 本例程演示了如何使用支付宝离线安全库对二维码进行验证
 * 
 * */
void check_qrcode_demo(){

	int ret = 0;
	
	unsigned char qrcode[512] = {0};
	int qrcode_len = sizeof(qrcode);
	/*
	* info_response mot
	*/
	char cert_sn[7] = {0};
	char mot_code_issuer_no[9] = {0};
	char card_issuer_no[9] = {0};
	char mot_user_id[17] = {0};
	char mot_card_no[21] = {0};
	unsigned char mot_card_data[129] = {0};
	/*
	* info_response alipay
	*/
	char alipay_code_issuer_no[9] = {0};
	char card_type[9] = {0};
	char alipay_user_id[17] = {0};
	char alipay_card_no[20] = {0};
	unsigned char alipay_card_data[129] = {0};

	char proto_type[8] = {0};

	INFO_REQUEST info_request;
	INFO_RESPONSE info_response;
	CODE_INFO code_info;
	info_response.code_info = &code_info;

	char qrcode_hex[] = QRCODE_HEX_DATA;
	int qrcode_hex_len = strlen(qrcode_hex);

	hex_string_to_bytes_a(qrcode_hex, qrcode_hex_len, qrcode, qrcode_len);

	printf("===========准备数据================\n");
	
	printf("============进行POS初始化=============\n");	
	/**
	 * 请在POS启动时执行POS初始化
	 * 初始化时请提供初始化信息，见 INIT_REQUEST
	 */
	 ret = demo_init();
	 if(ret != SUCCESS){
	 	printf("初始化POS失败！\n");
	 	switch(ret){
	 		case ILLEGAL_PARAM:
	 			printf("初始化参数格式错误！请检查参数各字段是否正确。\n");
	 		break;
	 		case NO_ENOUGH_MEMORY:
	 			printf("内存不足，极端错误，请检查程序运行空间是否足够。\n");
	 		break;
	 		case SYSTEM_ERROR:
	 			printf("系统异常！请联系支付宝技术人员。\n");
	 		break;
	 		default:
	 		break;
	 	}
	 	return;
	 }

	/**
	 * 获取二维码信息
	 * INFO_REQUEST 二维码数据、长度
	 * INFO_RESPONSE 二维码内容，如果同时支持2种协议的二维码，初始化时请2种协议对应的返回参数都初始化
	 */
	info_request.qrcode = qrcode;
	info_request.qrcode_len = qrcode_hex_len/2;

	info_response.proto_type = proto_type;
	//mot
	info_response.code_info->mot_code_info.cert_sn = cert_sn;
	info_response.code_info->mot_code_info.cert_sn_len = sizeof(cert_sn);
	info_response.code_info->mot_code_info.card_issuer_no = card_issuer_no;
	info_response.code_info->mot_code_info.card_issuer_no_len = sizeof(card_issuer_no);
	info_response.code_info->mot_code_info.code_issuer_no = mot_code_issuer_no;
	info_response.code_info->mot_code_info.code_issuer_no_len = sizeof(mot_code_issuer_no);
	info_response.code_info->mot_code_info.user_id = mot_user_id;
	info_response.code_info->mot_code_info.user_id_len = sizeof(mot_user_id);
	info_response.code_info->mot_code_info.card_no = mot_card_no;
	info_response.code_info->mot_code_info.card_no_len = sizeof(mot_card_no);
	info_response.code_info->mot_code_info.card_data = mot_card_data;
	info_response.code_info->mot_code_info.card_data_len = sizeof(mot_card_data);
	//alipay
	info_response.code_info->alipay_code_info.card_type = card_type;
	info_response.code_info->alipay_code_info.card_type_len = sizeof(card_type);
	info_response.code_info->alipay_code_info.code_issuer_no = alipay_code_issuer_no;
	info_response.code_info->alipay_code_info.code_issuer_no_len = sizeof(alipay_code_issuer_no);
	info_response.code_info->alipay_code_info.user_id = alipay_user_id;
	info_response.code_info->alipay_code_info.user_id_len = sizeof(alipay_user_id);
	info_response.code_info->alipay_code_info.card_no = alipay_card_no;
	info_response.code_info->alipay_code_info.card_no_len = sizeof(alipay_card_no);
	info_response.code_info->alipay_code_info.card_data = alipay_card_data;
	info_response.code_info->alipay_code_info.card_data_len = sizeof(alipay_card_data);

	ret = get_qrcode_info(&info_request, &info_response);
	print_info_response(&info_response);
	if(ret != SUCCESS){
		printf("ret = %d\n", ret);
		switch(ret){
			case MALFORMED_QRCODE:
				printf("二维码格式错误！请提示用户二维码错误。\n");
			break;
			case NO_ENOUGH_MEMORY:
				printf("内存不足，极端错误，请检查程序运行空间是否足够。\n");
			break;
			case ILLEGAL_PARAM:
				printf("参数错误！请确认入参是否正确。\n");
			break;
			case SYSTEM_ERROR:
				printf("系统异常！请联系支付宝技术人员。\n");
			break;
			default:
			break;
		}
		printf("获取二维码信息结束！获取失败！\n");
		printf("===========例程结束================\n");
		uninit();
		return;
	}
	printf("get qrcode info ret = %d\n", ret);
	
	/**
	 * 获取二维码信息后，请根据二维码信息获取指定的密钥，在验证时传入
	 */

	/**
	 * pos_param中填入商户pos相关信息 至少包括：
	 *		- pos_id	(商户下唯一的pos号)
	 *		- type		(脱机记录类型，只刷一次闸机计费的场景下，类型为"SINGLE")
	 * 		- subject	(脱机记录标题，建议放入公交路线)
	 *		- record_id	(记录id，商户下本次脱机记录唯一id号，record_id必须保证商户唯一，建议通过POS，时间等信息拼装)
	 * 注意：pos_param的长度不能大于1024字节！
     */
	POS_PARAM_STRUCT pos_param_struct;
	// = (POS_PARAM_STRUCT *)malloc(sizeof(POS_PARAM_STRUCT));
	pos_param_struct.record_id = "sh001_20160514140218_000001";
	pos_param_struct.consumption_type = 0;
	pos_param_struct.pos_id = "20170000000001";
	pos_param_struct.pos_mf_id = "9998112123";
	pos_param_struct.pos_sw_version = "2.6.14.03arm";
	pos_param_struct.merchant_type = "22";
	pos_param_struct.currency = "156";
	pos_param_struct.amount = 2000;
	pos_param_struct.vehicle_id = "vid9702";
	pos_param_struct.plate_no = "粤A 095852";
	pos_param_struct.driver_id = "0236245394";
	pos_param_struct.line_info = "795";
	pos_param_struct.station_no = "asd";
	pos_param_struct.lbs_info = "aaaa";	
	pos_param_struct.record_type = "SUBWAY";


	printf("===========准备数据结束================\n");
	
	printf("===========校验二维码开始================\n");
	//拼装验证请求
	VERIFY_REQUEST_V3 verify_request;
	//装入二进制格式的二维码
	verify_request.qrcode = qrcode;
	//装入二进制二维码长度
	verify_request.qrcode_len = strlen(qrcode_hex)/2;
	//装入pos_param
	verify_request.pos_param_struct = &pos_param_struct;

	//verify_request.public_key = TEST_MOT_TRIPLE_SM2_KEY;
	 verify_request.public_key = TEST_ALIPAY_PUBLIC_KEY;
	VERIFY_RESPONSE_V3 verify_response;
	verify_response.record = (char*)malloc(2048);
	verify_response.record_len = 2048;

	/**
	 * 调用接口验证二维码的有效性
	 */
	ret = verify_qrcode_v3(&verify_request, &verify_response);

	/**
	 * 处理返回的结果
	 */
	if(ret != SUCCESS){
		printf("ret = %d\n", ret);
		switch(ret){
			case MALFORMED_QRCODE:
				printf("二维码格式错误！请提示用户二维码错误。\n");
			break;
			case QRCODE_INFO_EXPIRED:
				printf("二维码过期！请提示用户刷新二维码。\n");
			break;
			case QRCODE_KEY_EXPIRED:
				printf("二维码密钥过期！请提示用户联网后刷新二维码再使用。\n");
			break;
			case POS_PARAM_ERROR:
				printf("商户传入的pos_param错误，请检查传入的pos_param。\n");
			break;
			case QUOTA_EXCEEDED:
				printf("单笔额度超限！请提示用户由于额度限制无法过闸机。\n");
			break;
			case NO_ENOUGH_MEMORY:
				printf("内存不足，极端错误，请检查程序运行空间是否足够。\n");
			break;
			case ILLEGAL_PARAM:
				printf("参数错误！请确认入参是否正确。\n");
			break;
			case CARDTYPE_UNSUPPORTED:
				printf("此机具不支持二维码对应的卡类型！\n");
			break;
			case QRCODE_DUPLICATED:
				printf("二维码重复！请提示用户刷新二维码。\n");
			break;
			case SYSTEM_ERROR:
				printf("系统异常！请联系支付宝技术人员。\n");
			break;
			default:
			break;
		}
		printf("二维码校验结束！验证失败，不放行！\n");
		printf("===========验证二维码例程 结束================\n");
		free(verify_response.record);
		uninit();
		return;
	}
	printf("验证成功后，返还的脱机记录: %s\n", verify_response.record);

	/**
	 * 1.商户可以根据uid判断是否为同一用户重复交易
	 */
	
	/**
	 * 2.商户可以根据qrcode判断是否为重复二维码
	 *   此判断也可以放在校验二维码前执行，商户可以自行选择
	 */

	/**
	 * 3.商户需要根据卡类型、卡号、卡数据 综合判断该卡的合法性、以及是否受理该卡
	 * 请商户保留 可受理 的脱机记录
	 */
	free(verify_response.record);
	uninit();
	printf("验证成功，请放行！\n");
	printf("===========验证二维码例程 结束================\n");
}

int demo_init(){
	int ret = 0;
	INIT_REQUEST init_request;
	INIT_INFO *init_info_list[2];

	char* card_types[3] = {0};
	INIT_INFO *init_info_mot = (INIT_INFO *)malloc(sizeof(INIT_INFO));

	init_info_mot->proto_type = "MOT";
	init_info_mot->card_type_number = 0;
	init_info_mot->code_issuer_no = "50083010";

	init_info_list[0] = init_info_mot;

	INIT_INFO *init_info_alipay = (INIT_INFO *)malloc(sizeof(INIT_INFO));

	char* card_type_a = "ANT00001";
	char* card_type_b = "TEST0001";
	char* card_type_c = "S0JP0000";
	card_types[0] = card_type_a;
	card_types[1] = card_type_b;
	card_types[2] = card_type_c;
	init_info_alipay->proto_type = "ALIPAY";
	init_info_alipay->card_type_number = 3;
	init_info_alipay->code_issuer_no = "00000000";
	init_info_alipay->card_types = (const char **)card_types;

	init_info_list[1] = init_info_alipay;
	init_request.code_issuer_info_number = 2;
	init_request.code_issuer_infos = init_info_list;
	ret = init(&init_request);
	free(init_info_mot);
	return ret;
}
/**
* 字节数组转hex格式字符串
* @param print_buf: 十六进制字符串buffer
* @param print_buf_len: 十六进制字符串buffer长度
* @param bytes: 二进制数据
* @param bytes_len: 二进制数据长度
*/
/**
char* bytes_to_hex_string(
	char* print_buf, 
	int print_buf_len, 
	const unsigned char* bytes, 
	int len) {

	int i = 0;

	if(print_buf == NULL || bytes == NULL || (len * 2 + 1) > print_buf_len) {
		return NULL;
	}

	for(i = 0; i < len; i++) {
		print_buf[i * 2] = g_hex_map_table[(bytes[i] >> 4) & 0x0F];
		print_buf[i * 2 + 1] = g_hex_map_table[(bytes[i]) & 0x0F];
	}
	print_buf[i * 2] = '\0';
	return print_buf;
}
**/
/**
 * 判断这个char是否是hex格式
 * @param c 
 */

static int is_hex_format(char c){
	int ret = -1;
	if(c >= '0' && c <= '9') {
		ret = 1;
	}
	else if(c >= 'A' && c <= 'F') {
		ret = 1;
	}
	else if(c >= 'a' && c <= 'f') {
		ret = 1;
	}
	return ret;
}

/**
* hex格式字符串转字节数组
* @param hex_string: 十六进制字符串
* @param hex_string_len: 十六进制字符串长度
* @param bytes: 二进制数据存储空间
* @param bytes_len: 目标空间长度
*/

int hex_string_to_bytes_a(
	char* hex_string, 
	int hex_string_len, 
	unsigned char* bytes, 
	int bytes_len) {
	
	int i = 0;
	
	if((hex_string_len % 2 != 0) || (bytes_len * 2 < hex_string_len)) {

		printf("bytes_len = %d hex_string_len = %d\n", bytes_len, hex_string_len);
		return -1;
	}
	
	for(i = 0; i < hex_string_len; i += 2) {
		if(is_hex_format(hex_string[i]) != 1){
			return -1;
		}
		bytes[i/2] = ((hex_of_char(hex_string[i]) << 4) & 0xF0) | 
					(hex_of_char(hex_string[i + 1]) & 0x0F);
	}
	return 1;
	return 1;
}

static void print_info_response(INFO_RESPONSE *response){
	printf("response->proto_type = %s\n", response->proto_type);
	if(strcmp(response->proto_type, MOT_PROTO_TYPE) == 0){
		printf("二维码格式为交通部协议\n");

		printf("code_issuer_no = %s\n", response->code_info->mot_code_info.code_issuer_no);
		printf("card_issuer_no = %s\n", response->code_info->mot_code_info.card_issuer_no);
		printf("user_id = %s\n", response->code_info->mot_code_info.user_id);
		printf("card_no = %s\n", response->code_info->mot_code_info.card_no);

	}else if(strcmp(response->proto_type, ALIPAY_PROTO_TYPE) == 0){
		printf("二维码格式为支付宝协议\n");

		printf("key id = %d\n", response->code_info->alipay_code_info.key_id);
		printf("alg id = %d\n", response->code_info->alipay_code_info.alg_id);
		printf("card type = %s\n", response->code_info->alipay_code_info.card_type);
		printf("user_id = %s\n", response->code_info->alipay_code_info.user_id);
		printf("card_no = %s\n", response->code_info->alipay_code_info.card_no);

	}
}
/**
* hex格式char转二进制
*/
unsigned char hex_of_char(char c) {
	unsigned char tmp = 0;
	if(c >= '0' && c <= '9') {
		tmp = (c - '0');
	}
	else if(c >= 'A' && c <= 'F') {
		tmp = (c - 'A' + 10);
	}
	else if(c >= 'a' && c <= 'f') {
		tmp = (c - 'a' + 10);
	}
	return tmp;
}


void utils_hexdump(unsigned char *buf, int len) {
    int i = 0;

    printf("\n----------------------hexdump begin------------------------\n");
    for(i = 0; i < len; i++) {
        printf("%02x ", buf[i]);
        if( (i+1) % 16 == 0) {
            printf("\n");
        }
    }

    if(i%16 != 0) {
        printf("\n");
    }

    printf("---------------------hexdump end-------------------------\n\n");
}

void show_unzip_data(char *data, int len)
{
    char Unzip[4096] = {0};
    uLong nUnZipLen = 4096;

    uncompress((Byte*)Unzip, &nUnZipLen, (const Bytef*)data, len);

    printf("\n========================unzip data start===========================\n");
    printf("%s\n", Unzip);
    printf("\n========================unzip data end===========================\n");
}

void test_query_data(void) {
    printf("begin query_data\n");
    alipay_iot_query_params params;
    params.max_data_size = 4096;
    params.extras.product_id = NULL;
    params.extras.biz_type = NULL;
    params.extras.sub_type = NULL;

    char* out_data = NULL;
    int out_len = 0;

    int rv = alipay_iot_data_query(&params, &out_data, &out_len);
    printf("query_data rv is : %d out_len is : %d\n", rv, out_len);
    if (rv >= 0 && out_len > 0) {
        printf("\n========================compress data start===========================\n");
        utils_hexdump((unsigned char *)out_data, out_len);
        printf("\n========================compress data end===========================\n");
        show_unzip_data(out_data, out_len);
    }

    if(out_data)
    {
        alipay_iot_data_free(out_data);
    }

    printf("end query_data\n");
}

int main(int argc, char* argv[]) {
    TINIT();

    // 第一个参数是支付宝分配的pid，第二个参数是设备端的SN
    alipay_iot_init("bobmarshall", "helloworld");
	
    //log test
    TLIST(test_ctrl_open_log, "test_ctrl_open_log for set 1 to agent");
    TLIST(test_ctrl_close_log, "test_ctrl_close_log for set 0 to agent");
    TLIST(test_ctrl_set_fetch_log, "test_ctrl_set_fetch_log for set fetch_log range from agent");
    TLIST(test_ctrl_get_status_log, "test_ctrl_get_status_log for retrieve agent status");
    TLIST(test_set_config, "test_set_config for set_config to agent");
    TLIST(test_store_data, "test_store_data for store_data to agent");
    TLIST(test_query_data, "test_query_data for query_data from agent");
    //qrcode verify test
    check_qrcode_demo();

    Tcmd cmd;
    cmd.case_id=-1;
    
    if(parse_args(argc,argv,&cmd)==0){
        execute(cmd);
        return 0;
    }else if(parse_args(argc,argv,&cmd) < -1){
        printf("[E] args is wrong.\n");
        return 0;
    }
    
    print_help_info();
    printf("\n>>>");
    
    char input[128];
    for(;;){
        cmd.case_id = -1;
        cmd.ctl_chr = 'L';
        cmd.ctl_num = 1;
        setbuf(stdin, NULL);
        input[0]='\0';
        fgets(input,128,stdin);
        // printf("input is: %s\n",input);
        if(0==parse_cmd_str(input,&cmd)){
            execute(cmd);
        }else{
            printf("[E] args is wrong.\n");
        }
        printf("\n>>>");
    }

    return 0;
}

//###################### private func #########################
void tlog(const char *log){
    time_t t;
    time(&t);
    struct tm *tmp_time = localtime(&t);
    char s[100];
    strftime(s, sizeof(s), "%H:%M:%S", tmp_time);
    printf("\n[ClientTestLog][%s]%s\n", s, log);
}

#define IS_DIGIT(c) ((c) >= '0' && (c) <= '9')

// "-T50" 或 "-h"
int parse_single(const char *str,Tcmd *cmd){
    if(str==NULL || strlen(str) < 2 || cmd == NULL ) return -1;
    if(str[0]!='-') return -2;
    int num = 0;
    int loop = 0;
    int i;
    for(i=2;i<strlen(str) && str[i] != ':';i++){
        if(!IS_DIGIT(str[i]))  continue;
        num = num*10 + (str[i]-'0');
    }

    switch(str[1]){
        case 'T':
           cmd->case_id = num;
           break;
        case 'L':
           cmd->case_id = num;
           cmd->ctl_chr = 'L';
           {
               for(i += 1;i<strlen(str);i++){
                   if(!IS_DIGIT(str[i]))  continue;
                   loop = loop*10 + (str[i]-'0');
               }
           }
           cmd->ctl_num = loop;
           break;
        default:
           cmd->ctl_chr=str[1];
    }
}

// "-T50 -L10 -h"
int parse_cmd_str(char *str,Tcmd *cmd){
    if(str==NULL || cmd == NULL || strlen(str) < 2) return -1;
    char *begin= str;
    char *EOS = str + strlen(str);
    while(begin < EOS){
        while(*begin==' ' && begin<=EOS) begin++;
        char *end = begin;
        while((*end!=' ' && *end != '\0')&& end<=EOS ) end++;
        *end='\0';
//        printf("parse res:%s\n",begin);
        parse_single(begin,cmd);
        begin=end+1;
    }
    return 0;
}

int parse_args(int argc,char **argv,Tcmd *cmd){
    if(argc==1) return -1;
    if(cmd==NULL) return -2;
    cmd->ctl_chr=0;
    cmd->ctl_num=0;
    cmd->case_id=0;
    for(int i=1;i<argc;i++){
        parse_single(argv[i],cmd);
    }
    return 0;
}

void execute(Tcmd cmd){
    printf("[CMD]run %d-->%c:%d\n",cmd.case_id,cmd.ctl_chr,cmd.ctl_num);
    switch (cmd.ctl_chr) {
        case 'h':
            print_help_info();
            break;
        case 'L':
            for(int i=0;i<cmd.ctl_num;i++){
                TRUN(cmd.case_id);
            }
            break;
        case 'q':
            printf("ByeBye...\n");
            exit(0);
        default:
            if(cmd.case_id>=0){
                TRUN(cmd.case_id);
            }else{
                print_help_info();
            }
    }
}

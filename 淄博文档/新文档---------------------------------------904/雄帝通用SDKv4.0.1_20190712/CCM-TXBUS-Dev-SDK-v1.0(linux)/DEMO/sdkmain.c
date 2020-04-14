#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <dlfcn.h>
#include <time.h>

#define wechatsdk 1
#ifdef wechatsdk
#include "./include/sdk.h"
#include "./include/cJSON.h"
#endif

#ifdef wechatsdk
typedef int (*api_init_txccm_pos_sdk_t) (const char *key_list);
typedef int  (*api_txccm_verify_qrcode_t) (TXCCM_REQUEST* txccm_request, TXCCM_RESPONSE* txccm_response);
typedef TXCCM_RESPONSE*  (*api_SDK_QRCODE_OUT_New_t) ();
typedef void  (*api_SDK_QRCODE_OUT_Free_t) (TXCCM_RESPONSE *txccm_response);
#endif
static void* sdkBarcodeValidator;
static api_init_txccm_pos_sdk_t api_init_txccm_pos_sdk;
static api_txccm_verify_qrcode_t api_txccm_verify_qrcode;
static api_SDK_QRCODE_OUT_New_t api_SDK_QRCODE_OUT_New;
static api_SDK_QRCODE_OUT_Free_t api_SDK_QRCODE_OUT_Free;

static void print_hex(const char *label, const uint8_t *v, size_t len)
{
	size_t i;

	printf("%s hex: ", label);
	for (i = 0; i < len; ++i) {
		printf("%02x", v[i]);
	}
	printf("--end\n");
}

static void sdk_init() 
{
	//这里的key_list请将下载密钥接口返回的key列表放到这里。
	const char * key_list = "[{\"key_id\":1,\"key_type\":1,\"key_value\":\"04D74E35A7C6E6540A6FB255AC4C5C414460D246EA595EF3F008352A9BDD028603D471662CDA84DE87C6CBEFBDBCA5BBC82B7244C326392E11\"}, {\"key_id\":2,\"key_type\":1,\"key_value\":\"045E7E7E3F6701A6A606BA8F70AB161B07F914348FE27DBB20F4A25B46A6159DF10100F5B5B0F9F669E729154A726B5AF7BDFA589995B1DF0D\"}, \
						{\"key_id\":1,\"key_type\":2,\"key_value\":\"EE32D7E2AC5A46DCB3897B47BFF22336\"}]";
	//初始化接口
	int ret = api_init_txccm_pos_sdk(key_list);
	if (0==ret) 
	{
		//初始化密钥列表成功
	}
	else 
	{
		//初始化密钥列表失败
		printf("%d\n",ret);
	}
}
static void ccm_verify( char* qrcode)
{
	int ret = 1;
	char pos_id[] = "AAAAAAAAAAAAAA";
	//pos_trx_id 应该为bcd编码
	char pos_trx_id[] = "0123456789";

	TXCCM_REQUEST *txccm_request = NULL;
	TXCCM_RESPONSE *txccm_response = NULL;

	do {
		txccm_request = (TXCCM_REQUEST*)calloc(1, sizeof(TXCCM_REQUEST));
		if (txccm_request == NULL)
		{
			return ;
		}
		txccm_request->qrcode = qrcode;
		txccm_request->qrcode_len = strlen(qrcode);

		cJSON * root = cJSON_CreateObject();
		cJSON_AddItemToObject(root, "pos_id", cJSON_CreateString(pos_id));
		cJSON_AddItemToObject(root, "pos_trx_id", cJSON_CreateString(pos_trx_id));
		cJSON_AddItemToObject(root, "scan_time", cJSON_CreateNumber(time(NULL)));
		cJSON_AddItemToObject(root, "pay_fee", cJSON_CreateNumber(100));
		cJSON_AddItemToObject(root, "scene", cJSON_CreateNumber(1));
		cJSON_AddItemToObject(root, "scan_type", cJSON_CreateNumber(1));

		printf("%s\n", cJSON_Print(root));
		txccm_request->pos_param = cJSON_Print(root);

		txccm_response = api_SDK_QRCODE_OUT_New();
		if (txccm_response == NULL)
		{
			break;
		}

		ret = api_txccm_verify_qrcode(txccm_request, txccm_response);
		printf("ccm_verify ret:%d\n", ret);
		if (ret == EC_SUCCESS)
		{
			printf("ykt_id:%x\n", txccm_response->ykt_id);
			printf("card_id:%s\n", txccm_response->card_id);
			printf("max_payfee:%d\n", txccm_response->max_pay_fee);
			printf("biz_data_len:%d\n", txccm_response->biz_data_len);
			if (txccm_response->biz_data_len > 0)
			{
				print_hex("biz_data", txccm_response->biz_data, txccm_response->biz_data_len);
			}
			printf("record:%s\n", txccm_response->record);
			printf("record_len:%d\n", txccm_response->record_len);
			ret = 0;
		}
	} while (0);

	if (txccm_request != NULL)
	{
		free(txccm_request);
		txccm_request = NULL;
	}

	if (txccm_response != NULL)
	{
		api_SDK_QRCODE_OUT_Free(txccm_response);
		txccm_response = NULL;
	}
}

int main(int argc,char *argv[])
{
	char strBarcodeValidator[] = "./libtxccm_pos_sdk.so";
	int bValidatorFunctional = 0;

#ifdef wechatsdk
	sdkBarcodeValidator = dlopen(strBarcodeValidator, RTLD_LAZY);

	api_init_txccm_pos_sdk = (api_init_txccm_pos_sdk_t)dlsym( sdkBarcodeValidator, "init_txccm_pos_sdk");
	api_txccm_verify_qrcode = (api_txccm_verify_qrcode_t)dlsym( sdkBarcodeValidator, "txccm_verify_qrcode");
	api_SDK_QRCODE_OUT_New = (api_SDK_QRCODE_OUT_New_t)dlsym( sdkBarcodeValidator, "SDK_QRCODE_OUT_New");
	api_SDK_QRCODE_OUT_Free = (api_SDK_QRCODE_OUT_Free_t)dlsym( sdkBarcodeValidator, "SDK_QRCODE_OUT_Free");


	if (!api_init_txccm_pos_sdk || !api_txccm_verify_qrcode ||!api_SDK_QRCODE_OUT_New ||!api_SDK_QRCODE_OUT_Free) {
		bValidatorFunctional = 0;
	} else {
		bValidatorFunctional = 1;
	}
#endif

	if (bValidatorFunctional) {
		char qrcode1[] = "TXADAP3cXkEXXTkUMTrsQIEUTfr3bYLbzoP33oHTfrbToTTjjXTsLfQDngLfrjnXbYTcUBBQABIAABdjAQCVlSEQdnXKwPOly1SboAaQAATiAAAAAAAAAEAQocpKCUrG4dktT/PUoewGTdoReR0qeo2iBsnxwmNeSyZEqg08CmPTeTmMexmPJ1JlhVOYpGUQQFAFyz9whXz6CuAAAAADUr64g=";

printf("--- debug --- api_init_txccm_pos_sdk --- start\n");
		sdk_init();
printf("--- debug --- api_init_txccm_pos_sdk --- end\n");

printf("--- debug --- api_txccm_verify_qrcode --- start\n");
		ccm_verify(qrcode1);
printf("--- debug --- api_txccm_verify_qrcode --- end\n");
	
	}
    return 0;
} 


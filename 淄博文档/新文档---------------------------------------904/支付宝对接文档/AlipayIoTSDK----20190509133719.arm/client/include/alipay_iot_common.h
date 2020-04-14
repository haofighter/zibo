#ifndef ALIPAY_SDK_COMMON_H
#define ALIPAY_SDK_COMMON_H

#include <stdint.h>

#if defined(__cplusplus) || defined(c_plusplus)
extern "C" {
#endif

#ifndef ALIPAY_SDK_EXTERN
#if defined(WIN32) || defined(_WIN32) || defined(__WIN32__) || defined(WIN64)
#define ALIPAY_SDK_EXTERN __declspec(dllexport)
#else
#define ALIPAY_SDK_EXTERN __attribute__((visibility ("default")))
#endif
#endif

#include <time.h>

/* SDK统一错误码整理 */

/* 数据库相关错误码 */
#define DB_ERR_CODE                       (int)0x83000000

#define DB_LOG_OK                         0
#define DB_LOG_ERR_INTERNAL               (int)(0x0001 | DB_ERR_CODE)    //db interal error
#define DB_LOG_ERR_HANDLE                 (int)(0x0002 | DB_ERR_CODE)    //db handle error
#define DB_LOG_ERR_PATH                   (int)(0x0003 | DB_ERR_CODE)    //db open error
#define DB_LOG_ERR_TIME_OPT               (int)(0x0004 | DB_ERR_CODE)    //db query time error
#define DB_LOG_ERR_MEM                    (int)(0x0005 | DB_ERR_CODE)    //db memory error
#define DB_LOG_ERR_GET_BUFFER             (int)(0x0006 | DB_ERR_CODE)    //db buffer get error
#define DB_LOG_ERR_SET_TRFLAG             (int)(0x0007 | DB_ERR_CODE)    //db set transferd error
#define DB_LOG_ERR_QUERY_NUM              (int)(0x0008 | DB_ERR_CODE)    //db get count invalid
#define DB_LOG_ERR_LOG_NULL               (int)(0x0009 | DB_ERR_CODE)    //db log is empty
#define DB_LOG_ERR_COMPRESS               (int)(0x000A | DB_ERR_CODE)    //db log compress err
#define DB_LOG_ERR_STORE_PARA             (int)(0x000B | DB_ERR_CODE)    //db log store para err
#define DB_LOG_ERR_NO_DATA                (int)(0x000C | DB_ERR_CODE)    //db log query no data err

/* bus_control 错误定义 */
#define BUS_CTRL_CODE                     (int)0x81000000

#define BUS_CTRL_OK                       0
#define BUS_CTRL_NO_TICKET_PRICE          (int)(0x0001 | BUS_CTRL_CODE)    //no ticket price config
#define BUS_CTRL_NO_DEVICE_ID             (int)(0x0002 | BUS_CTRL_CODE)    //no device id config
#define BUS_CTRL_NO_SUPPLIER_ID           (int)(0x0003 | BUS_CTRL_CODE)    //no supplier id config
#define BUS_CTRL_NO_ITEM_ID               (int)(0x0004 | BUS_CTRL_CODE)    //no item id config

/**
 * desc : 目前SDK支持的控制命令
 */
typedef enum IotControlType {
#define ALIPAY_IOT_STATUS_OPENED 1
#define ALIPAY_IOT_STATUS_CLOSED 0
    ALIPAY_IOT_GET_STATUS = 0,
    ALIPAY_IOT_SET_STATUS = 1,
    ALIPAY_IOT_EXE_COMMAND = 2,
} IotControlType;

typedef struct alipay_iot_extra_params {
    // product_id 数据的关联的产品id（保证产品内唯一），如果为空则查找全部
    const char* product_id;
    // 数据关联的biz_type，如果为空则查找全部
    const char* biz_type;
    // 数据关联的sub_type，如果为空则查找全部
    const char* sub_type;
} alipay_iot_extra_params;

typedef struct alipay_iot_query_params {
    //查询数据的唯一参数，如果为空则查询全部
    alipay_iot_extra_params extras;
    //获取最大的输出大小（byte为单位），如果小于等于0，默认为4K。
    int max_data_size;
} alipay_iot_query_params;

#define MAX_PATH_LEN    512

#if defined(__cplusplus) || defined(c_plusplus)
}
#endif

#endif
#ifndef ALIPAY_SDK_CLIENT_H
#define ALIPAY_SDK_CLIENT_H

#include <alipay_iot_common.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * desc : 初始化IPC链接
 * param: isv唯一标志（推荐使用支付宝的pid-partner id）
 *        sn设备的唯一标志
 * return: 无
 */
ALIPAY_SDK_EXTERN void alipay_iot_init(const char*isvid, const char* sn);

/**
 * desc : 初始化IPC链接
 * param: 用于存储version的内存地址
 *        version的地址的内存长度
 * return: int 0表示成功，其他为错误码
 */
ALIPAY_SDK_EXTERN int alipay_iot_get_version(char *version, int buf_len);

#ifdef __cplusplus
}
#endif

#endif /* ALIPAY_SDK_CLIENT_H */

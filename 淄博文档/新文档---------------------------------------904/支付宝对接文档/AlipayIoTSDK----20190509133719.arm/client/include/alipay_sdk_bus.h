#ifndef ALIPAY_SDK_BUS_H
#define ALIPAY_SDK_BUS_H

#include <alipay_iot_common.h>
#include <alipay_iot_dict.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * desc : 控制统一的接口。
 * param: type操作类型：
 *        ALIPAY_IOT_GET_STATUS: int 1表示打开，0表示关闭
 *        ALIPAY_IOT_SET_STATUS: int 1表示打开，0表示关闭
 *        ALIPAY_IOT_EXE_COMMAND: json string
 *        {
 *          cmdname: "fecth_log",
            //startTime开始时间
            startTime: 2019-02-28 10:00:00
            //endTime结束时间
            endTime: 2019-02-29 10:00:00
 *        }
 *        value根据不同的type，有不同的类型
 * return: int 0表示成功，其他为错误码 见 alipay_iot_common.h
 */
ALIPAY_SDK_EXTERN int alipay_iot_control(int type, ...);

/**
 * desc : 向统一SDK发送配置信息的接口，配置一些静态数据
 * param: configs 发送的key-value配置
 * return: int 0表示成功，其他为错误码
 */
ALIPAY_SDK_EXTERN int alipay_iot_set_config(const aliot_dict* configs);

/**
 * desc : 向统一SDK发送数据，在发送的过程中会产生一个唯一的id
 * param: params 插入的数据的额外参数
 *        datas 存储信息的key-value格式
 * return: int 0表示成功，其他为错误码
 */
ALIPAY_SDK_EXTERN int alipay_iot_data_store(const alipay_iot_extra_params* params, const aliot_dict* datas);

/**
 * desc :  按照时间或者条数查询数据的接口
 * param:  params 查询的入参数
 *         out_data 返回结果(不大于4k)，失败时返回NULL
 *         out_len 返回结果的长度，失败时返回0
 * return: int 成功时 返回该条件下剩余满足该条件的数目 若返回结果为0 需要判断out_len进行数据预取
               失败时 返回负数
 */
ALIPAY_SDK_EXTERN int alipay_iot_data_query(const alipay_iot_query_params* params, char** out_data, int* out_len);

/**
 * desc : 释放又查找生成的内存
 * param: out_data由调用alipay_iot_data_query生成的
 * return: 无
 */
ALIPAY_SDK_EXTERN void alipay_iot_data_free(void* out_data);

#ifdef __cplusplus
}
#endif

#endif /* ALIPAY_SDK_BUS_H */
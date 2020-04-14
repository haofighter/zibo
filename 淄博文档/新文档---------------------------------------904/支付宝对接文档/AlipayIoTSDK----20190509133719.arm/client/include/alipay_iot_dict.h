//
// Created by liuenbao on 19-2-11.
//

#ifndef ALIPAY_IOT_DICT_H
#define ALIPAY_IOT_DICT_H

#include <stdint.h>

#if defined(__cplusplus) || defined(c_plusplus)
extern "C" {
#endif

/* Dict 错误码说明 */
enum
{
    ALIOT_DICT_OK              =      0,
    ALICT_DICT_SET_OK          =      1,       // dict_set接口设置返回成功
    ALIOT_DICT_ERR_MEM         =     -1,       // 内存开辟错误
    ALICT_DICT_ERR_PARAM       =     -2,       // 入参存在错误
};


typedef struct aliot_dict_entry_t {
    char *key;
    char *value;
} aliot_dict_entry;

typedef struct aliot_dict_t aliot_dict;

/**
 * desc : 创建一个新的dict对象
 * param: 无
 * return: 如果成功返回dict对象指针，否则返回NULL
 */
aliot_dict* aliot_dict_new();

/**
 * desc : 释放一个已经创建的对象
 * param: 需要释放的对象
 * return: 无
 */
void aliot_dict_del(aliot_dict* dict);

/**
 * desc : 释放dict内的所有键值
 * param: 需要释放的对象
 * return: 无
 */
void aliot_dict_clear(aliot_dict* dict);

/**
 * desc : 查找一个特定key的entry，可以指定prev指针
 * param: dict 存储的dict对象
 *        key 需要查找的key
 * return: 如果找到返回entry指针，否则返回NULL
 */
aliot_dict_entry *aliot_dict_get(const aliot_dict *dict, const char *key);

/**
 * desc : 获取一个指定位置的entry
 * param: dict 存储的dict对象
 *        index 指定的位置，不能超出dict存储的范围
 * return: 如果成功，则返回对象的指针，否则返回NULL
 */
aliot_dict_entry* aliot_dict_get_at(aliot_dict* dict, int index);

/**
 * desc : 获取dict对象包含entry的个数
 * param: dict 存储的dict对象
 * return: dict对象包含entry的个数
 */
uint32_t aliot_dict_count(aliot_dict* dict);

/**
 * desc : 增加一个key-value的entry，如果key存在，则返回0
 * param: dict 存储的dict对象
 *        key 需要存储的key
 *        value 需要存储的value
 * return: 如果成功，则返回1，如果key已经存在，则返回0，其他返回错误
           key 已经存在 返回0 -----该 value会同步更新
 */
int aliot_dict_set(aliot_dict* dict, const char *key, const char *value);

/**
 * desc : 从dict对象中，移除一个指定key的entry。
 * param: dict 存储的dict对象
 *        key 需要移除的key
 * return: 如果成功，则返回1，如果不存在删除的节点，否则返回0; 否则返回其他错误
 */
int aliot_dict_remove(aliot_dict* dict, const char *key);

/**
 * desc : 增加一个key-value的entry，如果key存在，则返回0，value为int类型
 * param: dict 存储的dict对象
 *        key 需要存储的key
 *        value 需要存储的value
 * return: 如果成功，则返回1，如果key已经存在，则返回0，其他返回错误
 */
int aliot_dict_set_int(aliot_dict* dict, const char *key, int value);

/**
 * desc : 增加一个key-value的entry，如果key存在，则返回0，value为int64_t类型
 * param: dict 存储的dict对象
 *        key 需要存储的key
 *        value 需要存储的value
 * return: 如果成功，则返回1，如果key已经存在，则返回0，其他返回错误
 */
int aliot_dict_set_int64(aliot_dict* dict, const char *key, int64_t value);

/**
 * desc : 增加一个key-value的entry，如果key存在，则返回0，value为float类型
 * param: dict 存储的dict对象
 *        key 需要存储的key
 *        value 需要存储的value
 * return: 如果成功，则返回1，如果key已经存在，则返回0，其他返回错误
 */
int aliot_dict_set_float(aliot_dict* dict, const char *key, float value);

/**
 * desc : 增加一个key-value的entry，如果key存在，则返回0，value为double类型
 * param: dict 存储的dict对象
 *        key 需要存储的key
 *        value 需要存储的value
 * return: 如果成功，则返回1，如果key已经存在，则返回0，其他返回错误
 */
int aliot_dict_set_double(aliot_dict* dict, const char *key, double value);

/**
 * desc : 获取指定key的一个value对象，使用char*输出
 * param: dict 存储的dict对象
 *        key 需要存储的key
 *        ovalue ovalue是输出的对象
 * return: 如果成功，则返回1，否则返回其他错误
 */
int aliot_dict_get_value(aliot_dict* dict, const char *key, char **ovalue);

/**
 * desc : 释放get_value所输出的ovalue
 * param: value 使用aliot_dict_get_value所输出的ovalue
 * return: 无
 */
void aliot_dict_free(void* value);

#if defined(__cplusplus) || defined(c_plusplus)
}
#endif

#endif //ALIPAY_IOT_DICT_H

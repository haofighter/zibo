package com.szxb.zibo.moudle.function.unionpay.http;

import com.yanzhenjie.nohttp.rest.Response;

public interface HttpListener<T> {

    void success(int what, Response<T> response);

    void fail(int what, String e);
}

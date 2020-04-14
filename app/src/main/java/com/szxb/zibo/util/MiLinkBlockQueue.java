package com.szxb.zibo.util;

import android.util.Log;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class MiLinkBlockQueue extends LinkedBlockingDeque {
    public MiLinkBlockQueue(int capacity) {
        super(capacity);
    }

    @Override
    public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
        Object o = super.poll(timeout, unit);
        return o;
    }
}

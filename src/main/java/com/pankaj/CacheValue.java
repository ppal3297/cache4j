package com.pankaj;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CacheValue {
    final Object data;
    final long expiry;

    public boolean isExpired(){
        return System.currentTimeMillis() > expiry;
    }
}

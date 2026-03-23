package com.pankaj;

import java.util.concurrent.ConcurrentHashMap;

public class CacheNode {
    private final ConcurrentHashMap<String, CacheValue> store = new ConcurrentHashMap<>();

    public Object get(String key){
        CacheValue val = store.get(key);
        if(val == null || val.isExpired()){
            store.remove(key);
            return null;
        }
        return store.get(key);
    }

    public void put(String key, Object value, long ttl){
        store.put(key,new CacheValue(value,System.currentTimeMillis() + ttl));
    }

}

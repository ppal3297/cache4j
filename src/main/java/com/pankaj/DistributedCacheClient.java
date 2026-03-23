package com.pankaj;

import java.util.ArrayList;
import java.util.List;

public class DistributedCacheClient {
    List<CacheNode> nodes = new ArrayList<>();
    private long ttl = 60000;

    public Object get(String key){
        CacheNode node = getNode(key);
        return node.get(key);
    }

    public void put(String key, Object value){
        CacheNode node = getNode(key);
        node.put(key, value, ttl);
    }

    private CacheNode getNode(String key){
        int index = Math.abs(key.hashCode())% nodes.size();
        return nodes.get(index);

    }
}

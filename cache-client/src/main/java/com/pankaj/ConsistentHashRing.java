package com.pankaj;

import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashRing {
    private final SortedMap<Integer,String> ring = new TreeMap<>();
    private final int virtualNodes = 100;

    public void addNode(String node){
        for(int i= 0; i < virtualNodes;i++){
            int hash = hash(node+"-"+i);
            ring.put(hash,node);
        }
    }
    public void removeNode(String node){
        for(int i = 0;i < virtualNodes; i++){
            ring.remove(hash(node+"-"+i));
        }
    }
    public String getNode(String key){
        if(ring.isEmpty()) return null;
        int hash = hash(key);
        SortedMap<Integer,String> tailMap = ring.tailMap(hash);
        int nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        return ring.get(nodeHash);
    }
    private int hash(String key){
        return key.hashCode();
    }
}

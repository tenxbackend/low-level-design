package org.example.lld.cache;


import java.util.*;

public class Cache {
    private CacheStorage storage;
    private CacheEvictionStrategy evictionStrategy;

    public Cache(CacheStorage cacheStorage, CacheEvictionStrategy cacheEvictionStrategy){
        this.storage = cacheStorage;
        this.evictionStrategy = cacheEvictionStrategy;
    }

    public String get(String key){
        String value = this.storage.get(key);
        this.evictionStrategy.keyAccessed(key);
        return value;
    }

    public void put(String key, String value){
        if(this.storage.isFull() && this.storage.get(key)==null){
            String keyToBeRemoved = evictionStrategy.evictKey();
            this.storage.remove(keyToBeRemoved);
        }
        this.storage.put(key, value);
        this.evictionStrategy.keyAccessed(key);
    }
}



interface CacheEvictionStrategy{
    String evictKey();
    void keyAccessed(String key);
}


interface CacheStorage{
    String get(String key);
    void put(String key, String value);
    void remove(String key);
    boolean isFull();
}

class InMemoryCacheStorage implements CacheStorage{
    private Map<String, String> map;
    private int capacity;

    public InMemoryCacheStorage(int capacity){
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    @Override
    public String get(String key) {
        return this.map.get(key);
    }

    @Override
    public void put(String key, String value) {
        this.map.put(key, value);
    }

    @Override
    public void remove(String key) {
        this.map.remove(key);
    }

    @Override
    public boolean isFull() {
        return this.map.size() == this.capacity;
    }
}


class LRUEvictionStrategy implements CacheEvictionStrategy{

    private Deque<String> accessOrderList;
    private Set<String> existingKeys;

    public LRUEvictionStrategy(){
        this.accessOrderList = new LinkedList<>();
        this.existingKeys = new HashSet<>();
    }

    @Override
    public String evictKey() {
        String keyToBeRemoved = this.accessOrderList.removeFirst();
        this.existingKeys.remove(keyToBeRemoved);
        return keyToBeRemoved;
    }

    @Override
    public void keyAccessed(String key) {
        if (this.existingKeys.contains(key)){
            this.accessOrderList.remove(key);
        }
        this.accessOrderList.addLast(key);
        this.existingKeys.add(key);
    }
}


class CacheDemo{
    public static void main(String[] args) {
        int capacity = 3;
        CacheStorage inMemoryStorage = new InMemoryCacheStorage(capacity);
        CacheEvictionStrategy lruEvictionStrategy = new LRUEvictionStrategy();

        Cache cache = new Cache(inMemoryStorage, lruEvictionStrategy);

        cache.put("1", "Apple");
        cache.put("2", "Banana");
        cache.put("3", "Cherry");
        cache.put("4", "Date");

        System.out.println("Should be none " + cache.get("1"));
        System.out.println(cache.get("4"));

    }
}
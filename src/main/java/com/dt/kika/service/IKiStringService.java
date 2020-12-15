package com.dt.kika.service;

public interface IKiStringService<K, V> {

    void set(K key, V value);

    V get(K key);

    void del(K key);


}

package com.dt.kika.service;

public interface IKiHashService<K, F, V> {

    boolean hSet(K key, F field, V value);

    V hGet(K key, F field);

    void hDel(K key, F field);


}

package com.dt.kika.service;

public interface IKiKaService<K, V> {
    void save(K key, V value);

    V find(K key);

    void delete(K key);
}

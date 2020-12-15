package com.dt.kika.service.impl;

import com.dt.kika.common.constant.RocksDbConstant;
import com.dt.kika.service.IKiStringService;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Repository
public class KiStringServiceImpl implements IKiStringService<String, String> {

    /**
     * rocksdb数据存储目录;
     **/
    @Value("${kika.rocksdb.data.directory}")
    public String ROCKSDB_DATA_DIRECTORY;

    RocksDB stringDb;

    @PostConstruct
    void initialize() {
        RocksDB.loadLibrary();
        final Options options = new Options();
        options.setCreateIfMissing(true);
        if (StringUtils.isEmpty(ROCKSDB_DATA_DIRECTORY)) {
            log.error("rocksDb directory is empty");
            return;
        }

        try {
            File dbDir = new File(ROCKSDB_DATA_DIRECTORY, RocksDbConstant.STRING_DB);
            Files.createDirectories(dbDir.getParentFile().toPath());
            Files.createDirectories(dbDir.getAbsoluteFile().toPath());
            stringDb = RocksDB.open(options, dbDir.getAbsolutePath());
        } catch (IOException | RocksDBException ex) {
            log.error("Error initializng RocksDB, check configurations and permissions, exception: {}, message: {}, stackTrace: {}",
                    ex.getCause(), ex.getMessage(), ex.getStackTrace());
        }

        log.info("RocksDB initialized and ready to use");
    }

    @Override
    public synchronized void set(String key, String value) {
        log.info("save");
        try {
            stringDb.put(key.getBytes(), value.getBytes());
        } catch (RocksDBException e) {
            log.error("Error saving entry in RocksDB, cause: {}, message: {}", e.getCause(), e.getMessage());
        }
    }

    @Override
    public synchronized String get(String key) {
        log.info("find");
        String result = null;
        try {
            byte[] bytes = stringDb.get(key.getBytes());
            if (bytes == null) return null;
            result = new String(bytes);
        } catch (RocksDBException e) {
            log.error("Error retrieving the entry in RocksDB from key: {}, cause: {}, message: {}", key, e.getCause(), e.getMessage());
        }
        return result;
    }

    @Override
    public synchronized void del(String key) {
        log.info("delete");
        try {
            stringDb.delete(key.getBytes());
        } catch (RocksDBException e) {
            log.error("Error deleting entry in RocksDB, cause: {}, message: {}", e.getCause(), e.getMessage());
        }
    }
}

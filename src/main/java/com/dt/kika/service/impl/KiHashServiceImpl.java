package com.dt.kika.service.impl;

import com.dt.kika.common.constant.RocksDbConstant;
import com.dt.kika.common.util.XStringUtils;
import com.dt.kika.common.util.XTimeUtils;
import com.dt.kika.entity.KiHashValue;
import com.dt.kika.service.IKiHashService;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Repository
public class KiHashServiceImpl implements IKiHashService<String, String, String> {

    /**
     * rocksdb数据存储目录;
     **/
    @Value("${kika.rocksdb.data.directory}")
    public String ROCKSDB_DATA_DIRECTORY;

    RocksDB hashDb;

    @Override
    public boolean hSet(String metaKey, String field, String dataValue) {
        try {
            int curHashSize = 0;
            boolean isAddOperator = false;
            KiHashValue kiHashValue = new KiHashValue();
            if (get(metaKey, field, kiHashValue)) {
                if (kiHashValue.getFieldValue() != null) {
                    /** metaKey已经存在,field也存在; **/
                    isAddOperator = false;
                } else {
                    /** metaKey已经存在,field不存在; **/
                    isAddOperator = true;
                }
                curHashSize = XStringUtils.fourBytesToInt(kiHashValue.getMetaValue().getBytes());
            } else {
                /** metaKey和field都不存在,则进行创建写入; **/
                curHashSize = 0;
                isAddOperator = true;
            }
            hashDb.put(metaKey.getBytes(), generateMetaKeyValue(curHashSize, isAddOperator));
            writeDateKeyValue(metaKey.getBytes(), field, dataValue);
            return true;
        } catch (Exception e) {
            log.error("{}", e);
        }
        return false;
    }

    /**
     * 写入filed对应的值;
     *
     * @param metaKey
     * @param field
     * @param dataValue
     * @return
     */
    private boolean writeDateKeyValue(byte[] metaKey, String field, String dataValue) {
        try {
            ByteArrayOutputStream DataValue = new ByteArrayOutputStream();
            DataValue.write(XStringUtils.intTo4Bytes(metaKey.length));
            DataValue.write(metaKey);
            DataValue.write(field.getBytes());
            hashDb.put(DataValue.toByteArray(), dataValue.getBytes());
            return true;
        } catch (Exception e) {
            log.error("{}", e);
        }
        return false;
    }

    /**
     * 生成一级key对应的值;
     *
     * @param cutSize
     * @return
     */
    private byte[] generateMetaKeyValue(int cutSize, boolean isAddOperator) {
        try {
            int size = (isAddOperator) ? (cutSize + 1) : cutSize;
            log.info("write metaKey size [{}],isAddOper:[{}]", size, isAddOperator);
            ByteArrayOutputStream value = new ByteArrayOutputStream();
            value.write(XStringUtils.intTo4Bytes(size));
            value.write(XTimeUtils.timeStamp2Bytes());
            return value.toByteArray();
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    private byte[] get(String key) {
        try {
            byte[] keyBytes = hashDb.get(key.getBytes());
            if (keyBytes == null) return null;
            return keyBytes;
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    @Override
    public String hGet(String key, String field) {
        try {
            KiHashValue kiHashValue = new KiHashValue();
            if (get(key, field, kiHashValue)) {
                return kiHashValue.getFieldValue();
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    public boolean get(String key, String field, KiHashValue retValue) {
        try {
            if (null == retValue) {
                log.error("input ret buffer is null");
                return false;
            }

            byte[] keyBytes = hashDb.get(key.getBytes());
            if (keyBytes == null) {
                return false;
            }
            retValue.setMetaValue(new String(keyBytes));
            ByteArrayOutputStream targetKey = new ByteArrayOutputStream();
            targetKey.write(XStringUtils.intTo4Bytes(key.getBytes().length));
            targetKey.write(key.getBytes());
            targetKey.write(field.getBytes());
            byte[] targetValue = hashDb.get(targetKey.toByteArray());
            if (targetValue != null) {
                retValue.setFieldValue(new String(targetValue));
            }
            return true;
        } catch (Exception e) {
            log.error("{}", e);
        }
        return false;
    }

    @Override
    public void hDel(String key, String field) {

    }

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
            File dbDir = new File(ROCKSDB_DATA_DIRECTORY, RocksDbConstant.HASH_DB);
            Files.createDirectories(dbDir.getParentFile().toPath());
            Files.createDirectories(dbDir.getAbsoluteFile().toPath());
            hashDb = RocksDB.open(options, dbDir.getAbsolutePath());
        } catch (IOException | RocksDBException ex) {
            log.error("Error initializng RocksDB, check configurations and permissions, exception: {}, message: {}, stackTrace: {}",
                    ex.getCause(), ex.getMessage(), ex.getStackTrace());
        }

        log.info("RocksDB initialized and ready to use");
    }
}

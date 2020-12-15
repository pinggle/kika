package com.dt.kika.controller;

import com.dt.kika.service.IKiStringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/kika")
public class KiKaController {

    private final IKiStringService<String, String> rocksDB;

    public KiKaController(IKiStringService<String, String> rocksDB) {
        this.rocksDB = rocksDB;
    }

    @PostMapping(value = "/{key}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> save(@PathVariable("key") String key, @RequestBody String value) {
        log.info("start RocksApi.save [{}]:[{}]", key, value);
        rocksDB.set(key, value);
        return ResponseEntity.ok(value);
    }

    @GetMapping(value = "/{key}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> find(@PathVariable("key") String key) {
        log.info("start RocksApi.find key:[{}]", key);
        String result = rocksDB.get(key);
        if (result == null) {
            log.info("end RocksApi.find key:[{}] with empty value", key);
            return ResponseEntity.noContent().build();
        }
        log.info("end RocksApi.find key:[{}]:[{}]", key, result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{key}")
    public ResponseEntity<String> delete(@PathVariable("key") String key) {
        log.info("start RocksApi.delete key:[{}]", key);
        rocksDB.del(key);
        return ResponseEntity.ok(key);
    }
}

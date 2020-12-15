package com.dt.kika.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yanping
 * @date 2020/12/15 7:45 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@ContextConfiguration
@Slf4j
public class KiHashServiceImplTest {

    @Autowired
    KiHashServiceImpl kiHashService;

    @Test
    public void testFunc() {
        String metaKey = "test";
        String field = "msg3";
        String retValue = kiHashService.hGet(metaKey, field);
        log.info("before read out value is [{}]", retValue);
        kiHashService.hSet(metaKey, field, "hive");
        retValue = kiHashService.hGet(metaKey, field);
        log.info("after read out value is [{}]", retValue);
    }

}

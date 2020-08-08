package com.kakaofriends_mockup_project.api;

import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout = "10000")
@SpringBootTest 
public class ApiControllerTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getCategoryItemsTest() throws Exception {
        IntStream.range(0, 10).parallel().forEach(i -> {
            logger.info("{} 번째 요청", i + 1);
            long startTime = System.currentTimeMillis();
            this.webTestClient.get().uri("/api/category/items?t=1596428228035")
                    .exchange()
                    .expectStatus().isOk();
            long endTime = System.currentTimeMillis();
            logger.info("걸린시간 : {}", endTime - startTime);
        });
    }

}

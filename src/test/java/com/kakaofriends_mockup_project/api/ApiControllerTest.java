package com.kakaofriends_mockup_project.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout = "10000")
@WebFluxTest
public class ApiControllerTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getCategoryItemsTest() throws Exception {
        for(int i = 0; i < 10000; i++) {
            logger.info("{} 번째 요청", i + 1);
            long startTime = System.currentTimeMillis();
            this.webTestClient.get().uri("/api/category/items?t=1596428228035")
                    .exchange()
                    .expectStatus().isOk();
            long endTime = System.currentTimeMillis();
            logger.info("걸린시간 : {}", endTime - startTime);
        }
    }

}

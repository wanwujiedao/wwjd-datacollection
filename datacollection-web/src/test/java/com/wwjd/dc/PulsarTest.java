package com.wwjd.dc;

import com.alibaba.fastjson.JSON;
import com.wwjd.dc.controller.PulsarController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sun.misc.BASE64Encoder;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * dataCollection Tes
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-12-17 18:52:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PulsarTest {

    /**
     * dc
     */
    @Autowired
    private PulsarController pulsarController;

    /**
     * base  64  encode
     */
    private final static BASE64Encoder ENCODER = new BASE64Encoder();

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * requst
     *
     * @author adao
     * @time 2018/12/17 19:18
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    @Test
    public void pulsarTest() throws Exception {

        // parameters
        Map<String,String> map = new HashMap<>();
        map.put("name","test");
        map.put("age","18");
        map.put("version","9.10");

        // send request
        mockMvc.perform(
                post("/dc").header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMzM2IiwiaWF0IjoxNTQ1MDQ1MzI4LCJzdWIiOiJ7XCJuaWNrbmFtZVwiOlwi5YyX6L6wXCIsXCJuYW1lXCI6XCLmsarmmajpmLNcIixcIm1hdGVJZFwiOjEzMzYsXCJwcml2aWxlZ2VJZHNcIjpbXSxcIm9yZ0lkXCI6MX0ifQ.cPmkqFo2bT8MqLnKcAFmUUNTY0-oQwFyRUuimtAxZKU")
                        .param("spm",ENCODER.encode(JSON.toJSONString(map).getBytes()))
        ).andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}

package com.wwjd.datacollection;

import com.alibaba.fastjson.JSON;
import com.wwjd.datacollection.controller.DataCollectionController;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * dataCollection Tes
 *
 * @author adao
 * @CopyRight qtshe
 * @Created 2018-12-17 18:52:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PulsarTest {

    /**
     * datacollection
     */
    @Autowired
    private DataCollectionController dataCollectionController;

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
     * @CopyRight 杭州弧途科技有限公司（qtshe）
     * @param
     * @return
     */
    @Test
    public void pulsarTest() throws Exception {

        // parameters
        Map<String,String> map = new HashMap<>();
        map.put("ve","vest");
        map.put("a","appkey");
        map.put("c","channel");
        map.put("device_id","device_id");
        map.put("t","town_id");
        map.put("lo","lon");
        map.put("la","lat");
        map.put("o","os_version");
        map.put("v","version");
        map.put("s","session_id");
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> m = new HashMap<>();
        m.put("p","position");
        m.put("c","content_id");
        m.put("e","event_type");
        m.put("ri","refer_id");
        m.put("ci","current_id");
        m.put("bt","business_type");
        m.put("bi","business_id");
        m.put("d","duration");
        m.put("t",String.valueOf(System.currentTimeMillis()));
        m.put("r","remark");
        list.add(m);
        Map<String,String> m1 = new HashMap<>();
        m1.put("p","position1");
        m1.put("c","content_id1");
        m1.put("e","event_type1");
        m1.put("ri","refer_id1");
        m1.put("ci","current_id1");
        m1.put("bt","business_type1");
        m1.put("bi","business_id1");
        m1.put("d","duration1");
        m1.put("t",String.valueOf(System.currentTimeMillis()));
        m1.put("r","remark1");
        list.add(m1);
        map.put("e",JSON.toJSONString(list));

        // send request
        mockMvc.perform(
                post("/datacollection/day").header("Authorization","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NzI3MDYiLCJpYXQiOjE1NDUwMzM0MzMsInN1YiI6IntcImFjY291bnRJZFwiOjY3MjcwNixcInVzZXJJZFwiOjIyNzA1MyxcInZlcnNpb25cIjoxfSJ9.tZsXyf5fsSUTaOASDGOVORM5c5vkiMeeZ6i4JmKPX-k")
                        .param("spm",ENCODER.encode(JSON.toJSONString(map).getBytes()))
        ).andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}

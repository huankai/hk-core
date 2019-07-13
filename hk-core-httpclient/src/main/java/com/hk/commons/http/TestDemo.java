package com.hk.commons.http;

import com.hk.commons.http.get.SimpleGetHttpExecutor;

import java.io.IOException;

/**
 * @author kevin
 * @date 2019-7-13 15:17
 */
public class TestDemo {

    public static void main(String[] args) throws IOException {
        SimpleGetHttpExecutor executor = new SimpleGetHttpExecutor();
        for (int i = 0; i < 10; i++) {
            System.out.println(executor.execute("https://www.baidu.com", null));
        }
    }
}

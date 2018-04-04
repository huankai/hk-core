package com.hk.core.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hk.core.redis.RedisApplication;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RedisApplication.class })
public class RedisClusterTest {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void test() {
		System.out.println(stringRedisTemplate.boundValueOps("age").get());
	}

}

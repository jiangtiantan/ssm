package com.imooc.ssm.dao.cache;

import com.imooc.ssm.bean.Seckill;
import com.imooc.ssm.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/springConfig/spring-mybatis.xml"})
public class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillService seckillService;
    private Long id=1L;
    @Test
    public void testSeckill() throws Exception {


        Seckill seckill=redisDao.getSeckill(id);
        if (seckill==null){
            seckill=seckillService.getById(id);
            if (seckill!=null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill=redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
    }
    @Test
    public void testConnection(){

        Jedis jedis = new Jedis("192.168.12.128",6379);
        jedis.auth("123");

//        System.out.println(jedis);
        jedis.set("test",new Date().toString());
        System.out.println(jedis.get("test"));
        Client client = jedis.getClient();
        System.out.println(client);
    }



}
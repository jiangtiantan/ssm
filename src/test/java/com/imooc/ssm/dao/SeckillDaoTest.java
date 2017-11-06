package com.imooc.ssm.dao;

import com.imooc.ssm.bean.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;

/*
* 配置spring和junit整合，Junit启动时加载springIOC容器
* */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springConfig/spring-mybatis.xml"})
public class SeckillDaoTest {
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {

        int updateCount=seckillDao.reduceNumber(1L,new Date());
        System.out.println("updateCount="+updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id=1;
        Seckill seckill=seckillDao.queryById(id);

        System.out.println(seckill.getName());
        System.out.println(seckill.getNumber());
    }

    @Test
    public void queryAll() throws Exception {
    }

}
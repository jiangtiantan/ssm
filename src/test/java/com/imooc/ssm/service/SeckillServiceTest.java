package com.imooc.ssm.service;

import com.imooc.ssm.bean.Seckill;
import com.imooc.ssm.dto.Exposer;
import com.imooc.ssm.dto.SeckillExcution;
import com.imooc.ssm.exception.RepeatKillException;
import com.imooc.ssm.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springConfig/spring-mybatis.xml","classpath:springConfig/spring-service.xml"})
public class SeckillServiceTest {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills=seckillService.getSeckillList();
        logger.info("seckill={}",seckills);

    }

    @Test
    public void getById() throws Exception {
        long id=1;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testSeckillExecution() throws Exception {
        long id=3;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);

        if(exposer.isExposed()){
            try{

                SeckillExcution seckillExcution = seckillService.executeSeckill(id,13564925208L,exposer.getMd5());
                logger.info("seckillExcution={}",seckillExcution);
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }

    }


}
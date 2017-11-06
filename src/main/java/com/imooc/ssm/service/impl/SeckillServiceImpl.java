package com.imooc.ssm.service.impl;

import com.imooc.ssm.bean.Seckill;
import com.imooc.ssm.bean.SuccessKilled;
import com.imooc.ssm.dao.SeckillDao;
import com.imooc.ssm.dao.SuccessKilledDao;
import com.imooc.ssm.dao.cache.RedisDao;
import com.imooc.ssm.dto.Exposer;
import com.imooc.ssm.dto.SeckillExcution;
import com.imooc.ssm.enums.SeckillStateEnum;
import com.imooc.ssm.exception.RepeatKillException;
import com.imooc.ssm.exception.SeckillCloseException;
import com.imooc.ssm.exception.SeckillException;
import com.imooc.ssm.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 秒杀相关接口类
 * */
@Service(value = "seckillService")
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String slat = "ashdhcdhdcasdcah%^*&&^%^%^^";

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    //redisDao注入
    @Autowired
    private RedisDao redisDao;

    /**
     * 获取秒杀产品列表页
     * */
    public List<Seckill> getSeckillList() {

        List<Seckill> seckill = seckillDao.queryAll(0, 5);
        return seckill;
    }

    /**
     * 通过id获取秒杀详细信息
     * @param seckillId
     */
    public Seckill getById(Long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        return seckill;
    }

    /**
     * 秒杀接口暴露service
     * @param seckillId
     * */
    public Exposer exportSeckillUrl(Long seckillId) {
        //使用redis缓存
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {

            //如果缓存中没有，到数据库中查找
            seckill = seckillDao.queryById(seckillId);
            //如果数据库中有对应的记录,保存到缓存中
            if (seckill != null) {
                redisDao.putSeckill(seckill);
            } else {
                return new Exposer(false, seckillId);
            }

        }
        String md5 = getMd5(seckillId);
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (startTime.getTime() > nowTime.getTime() || nowTime.getTime() > endTime.getTime()) {

            return new Exposer(false, nowTime.getTime(), seckillId, startTime.getTime(), endTime.getTime());
        }
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 生成md5校验码
     * @param seckillId
     * @return
     */
    private String getMd5(Long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 执行秒杀接口
     * @param seckillId
     * @param iphone
     * @param md5
     * @return SeckillExcution
     * @throws RepeatKillException
     * @throws SeckillCloseException
     * @throws SeckillException
     */
    @Transactional
    public SeckillExcution executeSeckill(Long seckillId, Long iphone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("the md5 is not ");
        }
        try {

            //优化策略insert语句先执行，update有行级锁机制，后执行
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, iphone);
            if (insertCount <= 0) {
                throw new RepeatKillException("重复秒杀");
            } else {
                //秒杀成功,减库存
                int updateCount = seckillDao.reduceNumber(seckillId, new Date());

                if (updateCount <= 0) {
                    throw new SeckillCloseException("秒杀关闭");
                }
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId);
                return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            }
        }  catch (SeckillException e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException(e.getMessage());
        }

    }
}

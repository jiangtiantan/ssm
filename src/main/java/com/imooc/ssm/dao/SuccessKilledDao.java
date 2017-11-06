package com.imooc.ssm.dao;

import com.imooc.ssm.bean.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * 秒杀成功明细mapper接口
 * */
public interface SuccessKilledDao {

    /**
    * 插入购买明细
    * @param seckillId
    * @param iphone
    * */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("iphone") long iphone);

    /**
    根据id查询SuccessKilled 并返回seckill
     @param seckillId
    * */
    SuccessKilled queryByIdWithSeckill(long seckillId);
}

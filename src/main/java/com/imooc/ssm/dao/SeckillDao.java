package com.imooc.ssm.dao;

import com.imooc.ssm.bean.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    /*
    * 减少库存操作
    *@param seckillId
    * @param killTime
    * */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /*
    * 根据id查询秒杀库存表*/
    Seckill queryById(long seckillId);

    /*
    * 查询所有的库存信息*/
    List<Seckill> queryAll(@Param("off") int off, @Param("limit") int limit);
}

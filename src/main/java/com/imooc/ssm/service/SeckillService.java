package com.imooc.ssm.service;

import com.imooc.ssm.bean.Seckill;
import com.imooc.ssm.dto.Exposer;
import com.imooc.ssm.dto.SeckillExcution;
import com.imooc.ssm.exception.RepeatKillException;
import com.imooc.ssm.exception.SeckillCloseException;
import com.imooc.ssm.exception.SeckillException;

import java.util.List;

/*
业务接口：站在使用者角度设计接口
三个方面：方法的定义粒度，参数，范湖类型（return类型）
* */
public interface SeckillService {
    List<Seckill> getSeckillList();
    /** 查询单个秒杀记录

    * */
    Seckill getById(Long seckillId);

    /*
    * 秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
    * */
    Exposer exportSeckillUrl(Long seckillId);

    /*
    执行秒杀操作
    * */
    SeckillExcution executeSeckill(Long seckillId, Long iphone, String  md5) throws RepeatKillException,SeckillCloseException,SeckillException;
}

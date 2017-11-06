package com.imooc.ssm.bean;

import java.util.Date;

public class SuccessKilled {
    private long seckillId;
    private long iphone;
    private char state;
    private Date createTime;

    //多对一关系 多个成功明细对应一个商品

    private Seckill seckill;

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", iphone=" + iphone +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }

    public long getSeckillId() {
        return seckillId;
    }

    public long getIphone() {
        return iphone;
    }

    public char getState() {
        return state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public void setIphone(long iphone) {
        this.iphone = iphone;
    }

    public void setState(char state) {
        this.state = state;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

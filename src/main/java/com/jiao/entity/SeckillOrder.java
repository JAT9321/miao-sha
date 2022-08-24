package com.jiao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * 秒杀订单表(SeckillOrder)表实体类
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
@SuppressWarnings("serial")
@TableName("t_seckill_order")
public class SeckillOrder extends Model<SeckillOrder> {
    //秒杀订单ID
    private Long id;
    //用户ID
    private Long userId;
    //订单ID
    private Long orderId;
    //商品ID
    private Long goodsId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }


}


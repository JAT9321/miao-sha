package com.jiao.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * 秒杀商品表(SeckillGoods)表实体类
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
@SuppressWarnings("serial")
@TableName("t_seckill_goods")
public class SeckillGoods extends Model<SeckillGoods> {
    //秒杀商品ID
    private Long id;
    //商品ID
    @TableField("goods_id")
    private Long goodsId;
    //秒杀家
    private Double seckillPrice;
    //库存数量
    private Integer stockCount;
    //秒杀开始时间
    private Date startDate;
    //秒杀结束时间
    private Date endDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Double getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Double seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}


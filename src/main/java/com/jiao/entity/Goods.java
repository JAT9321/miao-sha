package com.jiao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 商品表(Goods)表实体类
 *
 * @author makejava
 * @since 2022-08-15 20:42:39
 */
@SuppressWarnings("serial")
@TableName("t_goods")
public class Goods extends Model<Goods>{
    //商品ID
    private Long id;
    //商品名称
    private String goodsName;
    //商品标题
    private String goodsTitle;
    //商品图片
    private String goodsImg;
    //商品详情
    private String goodsDetail;
    //商品价格
    private Double goodsPrice;
    //商品库存，-1表示没有限制
    private Integer goodsStock;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(Integer goodsStock) {
        this.goodsStock = goodsStock;
    }
    
}


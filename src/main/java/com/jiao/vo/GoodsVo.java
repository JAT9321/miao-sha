package com.jiao.vo;

import com.jiao.dao.GoodsDao;
import com.jiao.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * auth:@highSky
 * create:2022/8/15
 * email:zgt9321@qq.com
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
    /**
     * 秒杀价格
     **/
    private BigDecimal seckillPrice;

    /**
     * 剩余数量
     **/
    private Integer stockCount;

    /**
     * 开始时间
     **/
    private Date startDate;

    /**
     * 结束时间
     **/
    private Date endDate;
}

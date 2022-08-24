package com.jiao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiao.entity.SeckillOrder;
import com.jiao.entity.TUser;

/**
 * 秒杀订单表(SeckillOrder)表服务接口
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
public interface SeckillOrderService extends IService<SeckillOrder> {

    Long getResult(TUser tUser, Long goodsId);
}


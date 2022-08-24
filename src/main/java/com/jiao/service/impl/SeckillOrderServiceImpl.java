package com.jiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiao.dao.SeckillOrderDao;
import com.jiao.entity.SeckillOrder;
import com.jiao.entity.TUser;
import com.jiao.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 秒杀订单表(SeckillOrder)表服务实现类
 *
 * @author makejava
 * @since 2022-08-15 20:42:41
 */
@Service("seckillOrderService")
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderDao, SeckillOrder> implements SeckillOrderService {
    @Autowired
    SeckillOrderDao seckillOrderDao;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Override
    public Long getResult(TUser tUser, Long goodsId) {

        SeckillOrder seckillOrder = seckillOrderDao.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", tUser.getId()).eq("goods_id", goodsId));
        if (!ObjectUtils.isEmpty(seckillOrder)) {
            return seckillOrder.getOrderId();
        } else if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
            return 1L;
        } else {
            return 0L;
        }

    }
}


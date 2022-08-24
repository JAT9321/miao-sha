package com.jiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiao.dao.OrderDao;
import com.jiao.entity.Order;
import com.jiao.entity.SeckillGoods;
import com.jiao.entity.SeckillOrder;
import com.jiao.entity.TUser;
import com.jiao.exception.GlobalException;
import com.jiao.service.GoodsService;
import com.jiao.service.OrderService;
import com.jiao.service.SeckillGoodsService;
import com.jiao.service.SeckillOrderService;
import com.jiao.utils.Md5Util;
import com.jiao.utils.UUIDUtil;
import com.jiao.vo.GoodsVo;
import com.jiao.vo.OrderDeatilVo;
import com.jiao.vo.ResBeanEnum;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    SeckillGoodsService seckillGoodsService;
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Order secKill(TUser user, GoodsVo goodsVo) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        seckillGoodsService.updateById(seckillGoods);
////        boolean updateRes = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
////                .setSql("stock_count=" + "stock_count-1")
////                .eq("goods_id", goodsVo.getId())
////                .gt("stock_count", 0)
////        );
////        if (!updateRes) {
////            return null;
////        }

        boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql(
                "stock_count=stock_count-1"
        ).eq("goods_id", goodsVo.getId())
                .gt("stock_count", 0));
//        if (!update) {
//            return null;
//        }
        if (seckillGoods.getStockCount() < 1) {
            redisTemplate.opsForValue().set("isStockEmpty:" + goodsVo.getId(), 0);
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderDao.insert(order);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder);
        return order;
    }

    @Override
    public OrderDeatilVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(ResBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderDao.selectById(orderId);
        GoodsVo goodsVobyGoodsId = goodsService.getGoodsByGoodId(order.getGoodsId());
        OrderDeatilVo orderDeatilVo = new OrderDeatilVo();
        orderDeatilVo.setTOrder(order);
        orderDeatilVo.setGoodsVo(goodsVobyGoodsId);
        return orderDeatilVo;
    }

    @Override
    public String createPath(TUser user, Long goodsId) {
        String md5 = Md5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, md5, 1, TimeUnit.MINUTES);
        return md5;
    }

    @Override
    public boolean checkPath(TUser user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String rPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);

        return path.equals(rPath);
    }
}


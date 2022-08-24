package com.jiao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiao.entity.Order;
import com.jiao.entity.TUser;
import com.jiao.vo.GoodsVo;
import com.jiao.vo.OrderDeatilVo;

/**
 * (Order)表服务接口
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
public interface OrderService extends IService<Order> {

    Order secKill(TUser user, GoodsVo goodsVo);

    OrderDeatilVo detail(Long orderId);

    String createPath(TUser user, Long goodsId);

    boolean checkPath(TUser user, Long goodsId, String path);
}


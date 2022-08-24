package com.jiao.rabbitmq;

import com.jiao.entity.SeckillOrder;
import com.jiao.entity.TUser;
import com.jiao.service.GoodsService;
import com.jiao.service.OrderService;
import com.jiao.utils.JsonUtil;
import com.jiao.vo.GoodsVo;
import com.jiao.vo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * auth:@highSky
 * create:2022/8/22
 * email:zgt9321@qq.com
 **/
@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private GoodsService goodsServicel;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("rabbit mq 收到消息" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        TUser user = seckillMessage.getTUser();
        GoodsVo goodsVo = goodsServicel.getGoodsByGoodId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
        //判断是否重复抢购
        SeckillOrder tSeckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (tSeckillOrder != null) {
            return;
        }
        //下单操作
        orderService.secKill(user, goodsVo);
    }

}

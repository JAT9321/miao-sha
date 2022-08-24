package com.jiao.controller;

import com.jiao.config.AccessLimit;
import com.jiao.entity.Goods;
import com.jiao.entity.Order;
import com.jiao.entity.SeckillOrder;
import com.jiao.entity.TUser;
import com.jiao.exception.GlobalException;
import com.jiao.rabbitmq.MQSender;
import com.jiao.service.GoodsService;
import com.jiao.service.OrderService;
import com.jiao.service.SeckillOrderService;
import com.jiao.utils.JsonUtil;
import com.jiao.vo.GoodsVo;
import com.jiao.vo.ResBean;
import com.jiao.vo.ResBeanEnum;
import com.jiao.vo.SeckillMessage;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * auth:@highSky
 * create:2022/8/16
 * email:zgt9321@qq.com
 **/
@Controller
@RequestMapping("/seckill")
@Slf4j
public class SecKillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    MQSender mqsender;
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    DefaultRedisScript<Long> defaultRedisScript;


    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

    @GetMapping("/captcha")
    public void getCaptcha(TUser user, Long goodsId, HttpServletResponse response) {
        //设置请求头为输出图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }

    }


    @GetMapping("path")
    @ResponseBody
    public ResBean getPath(TUser user, Long goodsId) {
        if (ObjectUtils.isEmpty(user)) {
            return ResBean.error(ResBeanEnum.SESSION_ERROR);
        }

        String str = orderService.createPath(user, goodsId);
        return ResBean.success(str);
    }


    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public ResBean doSeckill(@PathVariable String path, TUser user, @RequestParam(value = "goodsId") Long goodsId) {

        if (ObjectUtils.isEmpty(user)) {
            return ResBean.error(ResBeanEnum.SESSION_ERROR);
        }

        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return ResBean.error(ResBeanEnum.REQUEST_ILLEGAL);
        }
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        //重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return ResBean.error(ResBeanEnum.REPEATE_ERROR);
        }

//        Long count = valueOperations.decrement("seckillGoods:" + goodsId);
        Long count = redisTemplate.execute(defaultRedisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (count < 0) {
            EmptyStockMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return ResBean.error(ResBeanEnum.EMPTY_STOCK);
        }
        //内存标记，减少Redis的访问
        if (EmptyStockMap.get(goodsId)) {
            return ResBean.error(ResBeanEnum.EMPTY_STOCK);
        }

        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqsender.send(JsonUtil.object2JsonStr(seckillMessage));
        return ResBean.success(0);

    }

    @GetMapping("getResult")
    @ResponseBody
    public ResBean getResult(TUser tUser, Long goodsId) {
        if (tUser == null) {
            return ResBean.error(ResBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(tUser, goodsId);
        return ResBean.success(orderId);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goods = goodsService.getGoods();
        if (CollectionUtils.isEmpty(goods)) {
            return;
        }
        goods.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });
    }
}

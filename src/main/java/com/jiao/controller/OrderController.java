package com.jiao.controller;

import com.jiao.entity.TUser;
import com.jiao.exception.GlobalException;
import com.jiao.service.OrderService;
import com.jiao.vo.OrderDeatilVo;
import com.jiao.vo.ResBean;
import com.jiao.vo.ResBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * (Order)表控制层
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResBean detail(TUser user, Long orderId) {
        if (user == null) {
            throw new GlobalException(ResBeanEnum.SESSION_ERROR);
        }

        OrderDeatilVo orderDeatilVo = orderService.detail(orderId);
        return ResBean.success(orderDeatilVo);
    }
}


package com.jiao.controller;

import com.jiao.entity.TUser;
import com.jiao.service.GoodsService;
import com.jiao.vo.DetailVo;
import com.jiao.vo.GoodsVo;
import com.jiao.vo.ResBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafReactiveView;


//    @RequestMapping("toList")
//    public String toList(Model model, @CookieValue("userTicket") String ticket) {
//        if (StringUtils.isEmpty(ticket)) {
//            return "login";
//        }
//
////        TUser tuser = (TUser) session.getAttribute(ticket);
//        TUser tuser = (TUser) redisTemplate.opsForValue().get("user" + ticket);
//        if (ObjectUtils.isEmpty(tuser)) {
//            return "login";
//        }
//        model.addAttribute("user", tuser);
//        return "goodsList";
//
//    }

    //使用WebMvcConfigurer中的方法addArgumentResolvers，在带有user参数统一得到user
    @RequestMapping(value = "toList", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toList(Model model, TUser tuser, HttpServletRequest request, HttpServletResponse response) {

        String html = (String) redisTemplate.opsForValue().get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user", tuser);
        model.addAttribute("goodsList", goodsService.getGoods());
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafReactiveView.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)) {
            redisTemplate.opsForValue().set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/toDetail1/{goodId}", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toDetail1(Model model, TUser user, @PathVariable Long goodId,
                            HttpServletRequest request, HttpServletResponse response) {
        String html = (String) redisTemplate.opsForValue().get("goodsDetail:" + goodId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.getGoodsByGoodId(goodId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatus = 0;
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
        }

        model.addAttribute("goods", goodsVo);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafReactiveView.getTemplateEngine().process("goodsDetail", webContext);
        if (!StringUtils.isEmpty(html)) {
            redisTemplate.opsForValue().set("goodsList:" + goodId, html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodId}")
    @ResponseBody
    public ResBean toDetail(TUser user, @PathVariable Long goodId) {

        GoodsVo goodsVo = goodsService.getGoodsByGoodId(goodId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatus = 0;
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setTUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(secKillStatus);
        return ResBean.success(detailVo);

    }


}

package com.jiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;

/**
 * auth:@highSky
 * create:2022/8/11
 * email:zgt9321@qq.com
 **/

@Controller
@RequestMapping("test")
public class TestController {

    /**
     * 测试用的controller
     */
    @RequestMapping("/jiao")
    public String test1(Model model) {
        model.addAttribute("jiao", "娇娇");
        return "test";
    }

}

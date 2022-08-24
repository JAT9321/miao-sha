package com.jiao.controller;

import com.jiao.entity.TUser;
import com.jiao.service.TUserService;
import com.jiao.utils.Md5Util;
import com.jiao.utils.ValidatorUtil;
import com.jiao.vo.LoginBean;
import com.jiao.vo.ResBean;
import com.jiao.vo.ResBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * auth:@highSky
 * create:2022/8/12
 * email:zgt9321@qq.com
 **/
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    TUserService tUserService;

    @RequestMapping("toLogin")
    public String doLogin() {
        return "login";
    }

    @RequestMapping("doLogin")
    @ResponseBody
    public ResBean doLogin(@Valid LoginBean loginBean, HttpServletRequest request, HttpServletResponse response) {
        return tUserService.doLogin(loginBean, request, response);
    }

}

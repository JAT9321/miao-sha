package com.jiao.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiao.entity.TUser;
import com.jiao.service.TUserService;
import com.jiao.utils.CookieUtil;
import com.jiao.vo.ResBean;
import com.jiao.vo.ResBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * auth:@highSky
 * create:2022/8/24
 * email:zgt9321@qq.com
 **/
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    TUserService tUserService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            TUser user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }

            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, ResBeanEnum.SESSION_ERROR);
                }
                key += ":" + user.getId();
            }
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
            if (count == null) {
                redisTemplate.opsForValue().set(key, 1, second, TimeUnit.SECONDS);
            } else if (count < maxCount) {
                redisTemplate.opsForValue().increment(key);
            } else {
                render(response, ResBeanEnum.ACCESS_LIMIT_REACHED);
                return false;
            }
        }

        return true;
    }

    private void render(HttpServletResponse response, ResBeanEnum sessionError) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        ResBean bean = ResBean.error(sessionError);
        writer.write(new ObjectMapper().writeValueAsString(bean));
        writer.flush();
        writer.close();
    }

    private TUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String userTicket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        return tUserService.getUserByCookie(userTicket, request, response);
    }
}

package com.jiao.config;

import com.jiao.entity.TUser;

/**
 * auth:@highSky
 * create:2022/8/24
 * email:zgt9321@qq.com
 **/
public class UserContext {

    private static ThreadLocal<TUser> userThreadLocal = new ThreadLocal<>();

    public static void setUser(TUser user) {
        userThreadLocal.set(user);
    }

    public static TUser getUser() {
        return userThreadLocal.get();
    }

}

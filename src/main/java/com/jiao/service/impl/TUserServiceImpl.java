package com.jiao.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jiao.entity.TUser;
import com.jiao.dao.TUserDao;
import com.jiao.exception.GlobalException;
import com.jiao.service.TUserService;
import com.jiao.utils.CookieUtil;
import com.jiao.utils.Md5Util;
import com.jiao.utils.UUIDUtil;
import com.jiao.utils.ValidatorUtil;
import com.jiao.vo.LoginBean;
import com.jiao.vo.ResBean;
import com.jiao.vo.ResBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户表(TUser)表服务实现类
 *
 * @author makejava
 * @since 2022-08-12 18:13:56
 */
@Service("tUserService")
@Slf4j
public class TUserServiceImpl implements TUserService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Resource
    private TUserDao tUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TUser queryById(Long id) {
        return this.tUserDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tUser       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<TUser> queryByPage(TUser tUser, PageRequest pageRequest) {
        long total = this.tUserDao.count(tUser);
        return new PageImpl<>(this.tUserDao.queryAllByLimit(tUser, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    @Override
    public TUser insert(TUser tUser) {
        this.tUserDao.insert(tUser);
        return tUser;
    }

    /**
     * 修改数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    @Override
    public TUser update(TUser tUser) {
        this.tUserDao.update(tUser);
        return this.queryById(tUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.tUserDao.deleteById(id) > 0;
    }

    @Override
    public ResBean doLogin(LoginBean loginBean, HttpServletRequest request, HttpServletResponse response) {

        if (StringUtils.isEmpty(loginBean.getMobile()) || StringUtils.isEmpty(loginBean.getPassword())) {
            return ResBean.error(ResBeanEnum.LOGIN_ERROR);
        }
//        boolean isMobile = ValidatorUtil.isMobile(loginBean.getMobile());
//        if (!isMobile) {
//            return ResBean.error(ResBeanEnum.MOBILE_ERROR);
//        }

        TUser tUser = tUserDao.queryById(Long.parseLong(loginBean.getMobile()));

        if (ObjectUtils.isEmpty(tUser)) {
//            return ResBean.error(ResBeanEnum.LOGIN_ERROR);
            throw new GlobalException(ResBeanEnum.LOGIN_ERROR);
        }

        if (!Md5Util.formPassToBPass(loginBean.getPassword(), tUser.getSalt()).equals(tUser.getPassword())) {
//            return ResBean.error(ResBeanEnum.LOGIN_ERROR);
            throw new GlobalException(ResBeanEnum.LOGIN_ERROR);
        }

        //使用redis保存，分布式部署需求
        String userTicket = UUIDUtil.uuid();
//        request.getSession().setAttribute(userTicket, tUser);
        redisTemplate.opsForValue().set("user" + userTicket, tUser);
        CookieUtil.setCookie(request, response, "userTicket", userTicket);

        return ResBean.success(userTicket);
    }

    @Override
    public TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }

        TUser user = (TUser) redisTemplate.opsForValue().get("user" + userTicket);
        if (!ObjectUtils.isEmpty(user)) {
            //更新cookie信息
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}

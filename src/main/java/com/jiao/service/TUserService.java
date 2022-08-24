package com.jiao.service;

import com.jiao.entity.TUser;
import com.jiao.vo.LoginBean;
import com.jiao.vo.ResBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户表(TUser)表服务接口
 *
 * @author makejava
 * @since 2022-08-12 18:13:56
 */
public interface TUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TUser queryById(Long id);

    /**
     * 分页查询
     *
     * @param tUser       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<TUser> queryByPage(TUser tUser, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    TUser insert(TUser tUser);

    /**
     * 修改数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    TUser update(TUser tUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    ResBean doLogin(LoginBean loginBean, HttpServletRequest request, HttpServletResponse response);

    TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

}

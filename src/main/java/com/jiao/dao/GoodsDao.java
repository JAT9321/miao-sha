package com.jiao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiao.entity.Goods;
import com.jiao.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * 商品表(Goods)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-15 20:42:39
 */

public interface GoodsDao extends BaseMapper<Goods> {

    List<GoodsVo> getGoods();

    GoodsVo getGoodsByGoodId(Long goodId);
}


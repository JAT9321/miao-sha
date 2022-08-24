package com.jiao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiao.entity.Goods;
import com.jiao.vo.GoodsVo;

import java.util.List;

/**
 * 商品表(Goods)表服务接口
 *
 * @author makejava
 * @since 2022-08-15 20:42:39
 */
public interface GoodsService extends IService<Goods> {

    List<GoodsVo> getGoods();

    GoodsVo getGoodsByGoodId(Long goodId);

}


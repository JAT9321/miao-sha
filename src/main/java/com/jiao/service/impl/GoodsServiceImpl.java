package com.jiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiao.dao.GoodsDao;
import com.jiao.entity.Goods;
import com.jiao.service.GoodsService;
import com.jiao.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品表(Goods)表服务实现类
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public List<GoodsVo> getGoods() {

        return goodsDao.getGoods();
    }

    @Override
    public GoodsVo getGoodsByGoodId(Long goodId) {
        return goodsDao.getGoodsByGoodId(goodId);
    }
}


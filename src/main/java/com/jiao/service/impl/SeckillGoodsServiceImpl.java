package com.jiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiao.dao.SeckillGoodsDao;
import com.jiao.entity.SeckillGoods;
import com.jiao.service.SeckillGoodsService;
import org.springframework.stereotype.Service;

/**
 * 秒杀商品表(SeckillGoods)表服务实现类
 *
 * @author makejava
 * @since 2022-08-15 20:42:40
 */
@Service("seckillGoodsService")
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsDao, SeckillGoods> implements SeckillGoodsService {

}


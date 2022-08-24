package com.jiao.vo;

import com.jiao.entity.TUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * auth:@highSky
 * create:2022/8/20
 * email:zgt9321@qq.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private TUser tUser;

    private GoodsVo goodsVo;

    private int secKillStatus;

    private int remainSeconds;

}

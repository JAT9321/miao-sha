package com.jiao.exception;

import com.jiao.vo.ResBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * auth:@highSky
 * create:2022/8/13
 * email:zgt9321@qq.com
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {

    private ResBeanEnum resBeanEnum;

}

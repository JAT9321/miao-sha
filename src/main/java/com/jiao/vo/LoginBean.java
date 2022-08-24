package com.jiao.vo;

import com.jiao.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * auth:@highSky
 * create:2022/8/12
 * email:zgt9321@qq.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBean {

    @NotNull
    @IsMobile(required = true)
    private String mobile;

    @NotNull
    @Length(min= 4)
    private String password;
}

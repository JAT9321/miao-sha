package com.jiao.vo;

import com.jiao.utils.ValidatorUtil;
import com.jiao.validator.IsMobile;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * auth:@highSky
 * create:2022/8/13
 * email:zgt9321@qq.com
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobile(s);
        }
        if (StringUtils.isEmpty(s)) {
            return true;
        }
        return ValidatorUtil.isMobile(s);
    }
}

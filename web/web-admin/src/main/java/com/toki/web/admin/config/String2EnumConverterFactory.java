package com.toki.web.admin.config;

import com.toki.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 自定义转换器工厂， String --> BaseEnum <br>
 * 将前端http请求参数中的字符串（1、2）转换为相应的枚举类型
 * </p>
 *
 * @author toki
 */
@Component
public class String2EnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return code -> {
            for (T type : targetType.getEnumConstants()) {
                if (type.getCode().equals(Integer.valueOf(code))) {
                    return type;
                }
            }

            throw new IllegalArgumentException("code: " + code + "非法！");
        };
    }
}

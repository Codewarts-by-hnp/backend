package com.codewarts.noriter.common.converter;

import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import org.springframework.core.convert.converter.Converter;

public class StatusTypeConverter{

    public static class StringToStatusTypeConverter implements Converter<String, StatusType> {
        @Override
        public StatusType convert(String source) {
            source = source.toUpperCase();

            try {
                return StatusType.valueOf(source);
            } catch (IllegalArgumentException e) {
                throw new GlobalNoriterException(CommonExceptionType.INCORRECT_REQUEST_PARAM_TYPE);
            }
        }
    }
}

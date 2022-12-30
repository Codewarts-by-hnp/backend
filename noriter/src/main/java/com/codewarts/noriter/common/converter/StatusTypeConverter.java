package com.codewarts.noriter.common.converter;

import com.codewarts.noriter.article.domain.type.StatusType;
import org.springframework.core.convert.converter.Converter;

public class StatusTypeConverter{

    public static class StringToStatusTypeConverter implements Converter<String, StatusType> {
        @Override
        public StatusType convert(String source) {
           return StatusType.valueOfWithCaseInsensitive(source);
        }
    }
}

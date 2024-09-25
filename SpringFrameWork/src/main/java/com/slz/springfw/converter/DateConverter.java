package com.slz.springfw.converter;

import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
@Data
public class DateConverter implements Converter<String, LocalDate> {
    private String pattern;
    @Override
    public LocalDate convert(String source) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(source, formatter);
    }
}

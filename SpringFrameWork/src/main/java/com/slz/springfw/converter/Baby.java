package com.slz.springfw.converter;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
@Data
public class Baby {
    private String name;
    private String gender;
    private LocalDate birthday;
}

package com.slz.mybatis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/25
 */

@Data
@Accessors(chain = true)
public class Records {
    private int id;
    private String accno;
    private String category;
    private int money;
}

package com.slz.mybatis.mapper;

import com.slz.mybatis.entity.Records;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/25
 */

@Repository
public interface RecordsMapper {
    @Insert("insert into  records values (null, #{accno}, #{category}, #{money})")
    void insert(Records records);
}

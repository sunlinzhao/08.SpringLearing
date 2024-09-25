package com.slz.mybatis.mapper;

import com.slz.mybatis.entity.Acc;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/25
 */

@Repository
public interface AccMapper {
    @Update("update acc set money=money-#{money} where accno=#{accno}")
    void update(Acc acc);
}

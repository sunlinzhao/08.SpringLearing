package com.slz.springfw.annotation.service;

import com.slz.springfw.annotation.dao.TeacherDao;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/22
 */
@Setter
@Getter
public class TeacherService {
    private TeacherDao dao;
    public void teach(){
        dao.teach();
    }
}

package com.atwzs.reggie.service;

import com.atwzs.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName CategoryService
 * @Description
 * @Author WangZhisheng
 * @Date 11:33 2022/12/29
 * @Version 11.0.15
 */
public interface CategoryService extends IService<Category> {

    void remove(Long id);
}

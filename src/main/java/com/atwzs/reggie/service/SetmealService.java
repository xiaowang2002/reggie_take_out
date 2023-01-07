package com.atwzs.reggie.service;

import com.atwzs.reggie.entity.Setmeal;
import com.atwzs.reggie.entity.SetmealDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName SetmealService
 * @Description
 * @Author WangZhisheng
 * @Date 15:05 2022/12/29
 * @Version 11.0.15
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * @description: 新增套餐同时保存和菜品的关联
     **/
    void saveWithDish(SetmealDto setmealDto);

    /**
    * @description: 回显套餐以及关联的菜品
    **/
    SetmealDto getByIdWithSetmealDish(Long id);

    /**
    * @description: 修改套餐
    **/
    void updateWithSetmealDish(SetmealDto setmealDto);

    /**
    * @description: 删除套餐以及关联的菜品
    **/
    void removeWithDish(List<Long> ids);
}

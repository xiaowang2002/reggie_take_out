package com.atwzs.reggie.service;

import com.atwzs.reggie.dto.DishDto;
import com.atwzs.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName DishService
 * @Description
 * @Author WangZhisheng
 * @Date 15:04 2022/12/29
 * @Version 11.0.15
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish,dish_flavor
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    DishDto getByIdWithFlavor(Long id);

    //修改菜品
    void updateWithFlavor(DishDto dishDto);
}

package com.atwzs.reggie.service.impl;

import com.atwzs.reggie.dto.DishDto;
import com.atwzs.reggie.entity.Dish;
import com.atwzs.reggie.entity.DishFlavor;
import com.atwzs.reggie.mapper.DishMapper;
import com.atwzs.reggie.service.DishFlavorService;
import com.atwzs.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DishServiceImpl
 * @Description
 * @Author WangZhisheng
 * @Date 15:06 2022/12/29
 * @Version 11.0.15
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
    * @description: 新增菜品，同时保存对应的口味数据
    **/
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        save(dishDto);     //自动从dishdto获取插入要的字段完成insert语句

        Long dishid = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishid);
            return item;
        }).collect(Collectors.toList());

        //dishFlavorService.saveBatch(dishDto.getFlavors());
        //保存菜品口味到菜品数据表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
    * @description: 根据id查询菜品信息和口味信息
    **/
    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品街边呢信息，从dish表查询
        Dish dish = getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品口味信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息,自动匹配dishDto内的属性更新
        updateById(dishDto);

        //清理当前菜品对应口味数据--dish_flavor表的delete操作
        //delete from dish_flavor where dish_id = ?
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(wrapper);

        //添加当前提交过来的口味数据--dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}

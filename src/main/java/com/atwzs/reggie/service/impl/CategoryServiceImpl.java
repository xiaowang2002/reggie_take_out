package com.atwzs.reggie.service.impl;

import com.atwzs.reggie.common.CustomException;
import com.atwzs.reggie.entity.Category;
import com.atwzs.reggie.entity.Dish;
import com.atwzs.reggie.entity.Setmeal;
import com.atwzs.reggie.mapper.CategoryMapper;
import com.atwzs.reggie.service.CategoryService;
import com.atwzs.reggie.service.DishService;
import com.atwzs.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CategoryServiceImpl
 * @Description
 * @Author WangZhisheng
 * @Date 11:34 2022/12/29
 * @Version 11.0.15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * @description: 根据id删除分类，删除之前需要进行判断
     **/
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

        //添加查询条件，根据分类id进行查询
        //select count(*) from dish where category_id= 1397844263642378242;
        wrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(wrapper);

        //查询当前分类是否关联了菜品，如果已经关联抛出异常，
        if (count > 0) {
            //已经关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品");
        }

        //查询当前套餐是否关联了菜品，如果已经关联抛出异常，
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();

        //添加查询条件，根据分类id进行查询
        wrapper1.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count();

        if (count1 > 0) {
            //已经关联套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐");
        }

        //正常删除
        super.removeById(id);
    }
}

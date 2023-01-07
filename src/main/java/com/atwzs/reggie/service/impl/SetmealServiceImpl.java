package com.atwzs.reggie.service.impl;

import com.atwzs.reggie.common.CustomException;
import com.atwzs.reggie.common.R;
import com.atwzs.reggie.dto.DishDto;
import com.atwzs.reggie.entity.*;
import com.atwzs.reggie.mapper.SetmealMapper;
import com.atwzs.reggie.service.DishService;
import com.atwzs.reggie.service.SetmealDishService;
import com.atwzs.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SetmealServiceImpl
 * @Description
 * @Author WangZhisheng
 * @Date 15:06 2022/12/29
 * @Version 11.0.15
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * @description:
     **/
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        save(setmealDto);

        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getByIdWithSetmealDish(Long id) {
        //查询套餐菜品信息
        Setmeal setmeal = getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        //查询当前菜品口味信息
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithSetmealDish(SetmealDto setmealDto) {
        //更新setmeal表基本信息
        this.updateById(setmealDto);

        //更新setmeal_dish表信息delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        //更新setmeal_dish表信息insert操作
        List<SetmealDish> SetmealDishes = setmealDto.getSetmealDishes();

        SetmealDishes = SetmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(SetmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是是否可以删除
        //select count(*) from set_meal where id in (1,2,3) and status = 1
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, 1);
        int count = count(wrapper);

        //如果不能删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表的数据--setmeal
        removeByIds(ids);

        //删除关系表的数据--setmeal_dish
        //delete from setmeal_dish where setmeal_Id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(wrapper1);

    }

}

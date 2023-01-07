package com.atwzs.reggie.controller;

import com.atwzs.reggie.common.R;
import com.atwzs.reggie.dto.DishDto;
import com.atwzs.reggie.entity.Category;
import com.atwzs.reggie.entity.Dish;
import com.atwzs.reggie.entity.DishFlavor;
import com.atwzs.reggie.service.CategoryService;
import com.atwzs.reggie.service.DishFlavorService;
import com.atwzs.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.XMLFormatter;
import java.util.stream.Collectors;

/**
 * @ClassName DishController
 * @Description 菜品管理
 * @Author WangZhisheng
 * @Date 11:19 2022/12/31
 * @Version 11.0.15
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * @description: 新增菜品
     **/
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {

        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        Page<DishDto> dishDtoPage = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);

        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //进行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        //records属性是用来存放查询出来的数据，参数三是忽略某个属性
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //根据id查分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * @description: 根据id查询菜品信息和口味信息
     **/
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * @description: 修改菜品
     **/
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

//    /**
//     * @description: 根据条件查询对应的菜品数据
//     **/
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//
//        //构造查询条件
//        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
//                .eq(Dish::getStatus, 1)   //启售状态
//                .orderByAsc(Dish::getSort)
//                .orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(wrapper);
//        return R.success(list);
//    }

    /**
     * @description: 根据条件查询对应的菜品数据，升级版
     **/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        //构造查询条件
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, 1)   //启售状态
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(wrapper);

        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //根据id查分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL: select* from dishflavor where dish_id=?;
            List<DishFlavor> dishFlavorlist = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorlist);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

}

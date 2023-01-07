package com.atwzs.reggie.controller;

import com.atwzs.reggie.common.R;
import com.atwzs.reggie.entity.Category;
import com.atwzs.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CategoryController
 * @Description
 * @Author WangZhisheng
 * @Date 11:36 2022/12/29
 * @Version 11.0.15
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @description: 新增分类
     **/
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);  //分页构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);      //添加排序条件
        categoryService.page(pageInfo, wrapper);     //进行分页查询
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id) {
        log.info("删除分类,id为{}", id);

//        categoryService.removeById(id);  不全面、
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    //修改分类
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        //自动填充update修改时间和修改用户
        return R.success("分类修改成功");
    }

    /**
     * @description: 根据条件查询分类数据
     **/
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {      //自动把type=1封装在category实体类里面
        //条件构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType() != null, Category::getType, category.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}

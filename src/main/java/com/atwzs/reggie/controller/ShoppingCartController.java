package com.atwzs.reggie.controller;

import com.atwzs.reggie.common.BaseContext;
import com.atwzs.reggie.common.R;
import com.atwzs.reggie.entity.ShoppingCart;
import com.atwzs.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @description: 添加购物车
     **/
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据为：{}", shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long id = BaseContext.getCurrentId();
        shoppingCart.setUserId(id);  //设置用户id

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, id);

        //查询当前菜品或者套餐是否在购物车内已经存在，如果是则在原先的number上加1，如果不是则添加到购物车，默认数量为1
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            //添加到购物车的是菜品
            //WHERE USERID=ID AND DISH_ID=DISHID
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //添加到购物车的是套餐
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id ?/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(wrapper);

        if (cartServiceOne != null) {
            //已经存在，执行number+1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //如果不存在，则新增,number默认是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;   //此时的cartServiceOne有id
        }

        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
    * @description: 减少购物车
    **/
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long setmealId = shoppingCart.getSetmealId();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        if (setmealId!=null){
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }else {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        Integer number = one.getNumber();
        if(number==1){
            shoppingCartService.remove(queryWrapper);
        }else {
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }

        return R.success(one);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        //SQL:delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }
}
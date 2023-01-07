package com.atwzs.reggie.service.impl;

import com.atwzs.reggie.entity.ShoppingCart;
import com.atwzs.reggie.mapper.ShoppingCartMapper;
import com.atwzs.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}

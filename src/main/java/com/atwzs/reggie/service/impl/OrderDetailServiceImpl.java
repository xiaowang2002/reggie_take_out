package com.atwzs.reggie.service.impl;

import com.atwzs.reggie.entity.OrderDetail;
import com.atwzs.reggie.mapper.OrderDetailMapper;
import com.atwzs.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
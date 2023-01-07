package com.atwzs.reggie.service.impl;


import com.atwzs.reggie.entity.User;
import com.atwzs.reggie.mapper.UserMapper;
import com.atwzs.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

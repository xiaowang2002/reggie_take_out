package com.atwzs.reggie.service.impl;

import com.atwzs.reggie.entity.Employee;
import com.atwzs.reggie.mapper.EmployeeMapper;
import com.atwzs.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName EmployeeServiceImpl
 * @Description
 * @Author WangZhisheng
 * @Date 12:55 2022/12/27
 * @Version 11.0.15
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

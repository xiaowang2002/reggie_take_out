package com.atwzs.reggie.mapper;

import com.atwzs.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName EmployeeMapper
 * @Description
 * @Author WangZhisheng
 * @Date 12:53 2022/12/27
 * @Version 11.0.15
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

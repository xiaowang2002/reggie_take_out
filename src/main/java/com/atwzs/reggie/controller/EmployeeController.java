package com.atwzs.reggie.controller;

import com.atwzs.reggie.common.R;
import com.atwzs.reggie.entity.Employee;
import com.atwzs.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @ClassName EmployeeController
 * @Description
 * @Author WangZhisheng
 * @Date 12:56 2022/12/27
 * @Version 11.0.15
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * @description: 员工登录
     **/
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());  //数据库对username字段做了一个唯一的约束
        Employee emp = employeeService.getOne(wrapper);//查询唯一的一条数据，多个会抛出异常

        //3.如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("登录失败");
        }

        //4.密码比对，如果不一致则返回登录失败的结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        //5.查看员工状态，如果已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        //6.登陆成功，将员工id存入session并返回登录结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * @description: 员工退出
     **/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * @description: 新增员工
     **/
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息为：{}", employee.toString());

        //设置员工初始密码123456，然后进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //使用公共字段
//        //设置时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * @description: 员工信息分页查询
     **/
    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);

        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        wrapper.orderByDesc(Employee::getCreateTime);

        //执行查询
        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * @description: 根据id修改员工信息
     **/
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

       //被公共字段取代
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        log.info("线程Id为：{}",Thread.currentThread().getId());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * @description: 根据id查询员工信息
     **/
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息");
        Employee emp = employeeService.getById(id);
        if (emp == null) {
            R.error("没有查询到员工信息");
        }
        return R.success(emp);
    }
}

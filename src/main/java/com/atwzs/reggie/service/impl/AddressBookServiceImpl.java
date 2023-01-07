package com.atwzs.reggie.service.impl;


import com.atwzs.reggie.entity.AddressBook;
import com.atwzs.reggie.mapper.AddressBookMapper;
import com.atwzs.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}

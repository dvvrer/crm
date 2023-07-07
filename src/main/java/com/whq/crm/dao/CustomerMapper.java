package com.whq.crm.dao;

import com.whq.crm.base.BaseMapper;
import com.whq.crm.vo.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer,Integer> {
}
package com.whq.crm.dao;

import com.whq.crm.base.BaseMapper;
import com.whq.crm.vo.SaleChance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /**
     * 多添件查询的接口不需要单独定义
     * 由于多个模块涉及到多条件查询操作，所以将对应的多条件查询功能定义在副接口BaseMapper
     */
}
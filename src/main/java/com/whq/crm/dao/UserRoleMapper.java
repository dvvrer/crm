package com.whq.crm.dao;

import com.whq.crm.base.BaseMapper;
import com.whq.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //根据用户id查询用户角色记录
    Integer countUserRoleByUserId(Integer userId);

    //根据用户id删除用户角色记录
    Integer deleteUserRoleByUserId(Integer userId);
}
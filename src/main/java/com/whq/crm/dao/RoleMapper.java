package com.whq.crm.dao;

import com.whq.crm.base.BaseMapper;
import com.whq.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    //查询所有角色（只需要id和roleName）
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    //通过角色名查询角色记录
    public Role selectByRoleName(String roleName);
}
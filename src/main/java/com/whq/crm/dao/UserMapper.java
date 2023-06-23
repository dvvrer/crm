package com.whq.crm.dao;

import com.whq.crm.base.BaseMapper;
import com.whq.crm.vo.User;

public interface UserMapper extends BaseMapper<User,Integer> {

    //通过用户名查询用户记录，返回用户对象
    public User queryUserByName(String userName);
}
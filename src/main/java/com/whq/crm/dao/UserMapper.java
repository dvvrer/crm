package com.whq.crm.dao;

import com.whq.crm.base.BaseMapper;
import com.whq.crm.vo.User;

public interface UserMapper extends BaseMapper<User,Integer> {

    public User queryUserByName(String userName);
}
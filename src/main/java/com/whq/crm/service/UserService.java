package com.whq.crm.service;

import com.whq.crm.base.BaseService;
import com.whq.crm.dao.UserMapper;
import com.whq.crm.model.UserModel;
import com.whq.crm.utils.AssertUtil;
import com.whq.crm.utils.Md5Util;
import com.whq.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    public UserModel userLogin(String userName,String userPwd){
        //1、参数判断，判断用户姓名、用户密码非空
        checkLoginParams(userName,userPwd);

        //1、调用数据访问层，通过用户名查询用户记录，返回用户对象
        User user = userMapper.queryUserByName(userName);

        //3、判断用户对象是否为空
        AssertUtil.isTrue(user == null,"用户姓名不存在！");

        //4、判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
        checkUserPwd(userPwd,user.getPassword());

        //返回构建用户对象
        return buildUserInfo(user);
    }

    //构建需要返回给客户端的用户对象
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserId(user.getId());
        userModel.setUserName(user.getUsername());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void checkUserPwd(String userPwd, String password) {
        //将客户端传递的密码加密
        userPwd = Md5Util.encode(userPwd);
        //判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(userPwd),"用户密码不正确！");
    }

    private void checkLoginParams(String userName,String userPwd){
        //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空！");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空！");
    }
}

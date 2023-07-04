package com.whq.crm.service;

import com.whq.crm.base.BaseService;
import com.whq.crm.dao.UserMapper;
import com.whq.crm.model.UserModel;
import com.whq.crm.utils.AssertUtil;
import com.whq.crm.utils.Md5Util;
import com.whq.crm.utils.PhoneUtil;
import com.whq.crm.utils.UserIDBase64;
import com.whq.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        //1、参数判断，判断用户姓名、用户密码非空
        checkLoginParams(userName,userPwd);

        //2、调用数据访问层，通过用户名查询用户记录，返回用户对象
        User user = userMapper.queryUserByName(userName);

        //3、判断用户对象是否为空
        AssertUtil.isTrue(user == null,"用户姓名不存在！");

        //4、判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
        checkUserPwd(userPwd,user.getUserPwd());

        //返回构建用户对象
        return buildUserInfo(user);
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        //通过用户ID查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //判断用户记录是否存在
        AssertUtil.isTrue(null == user,"待更新记录不存在！");
        //参数校验
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        //设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //执行更新，判断受影响的行数
            AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败！");
    }

    /**
     * 修改密码的参数校验
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空！");
        //判断原始密码是否正确（查询的用户对象中的用户密码是否与原始密码一致）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确！");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空！");
        //判断新密码是否与原始密码一致（不允许新密码与原始密码一致）
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同！");
        //确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空！");
        //判断确认密码是否与新密码相同
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致！");
    }

    //构建需要返回给客户端的用户对象
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
//        userModel.setUserId(user.getId());
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void checkUserPwd(String userPwd, String password) {
        //将客户端传递的密码加密
        userPwd = Md5Util.encode(userPwd);
        //判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(password),"用户密码不正确！");
    }

    /**
     * 参数判断
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName,String userPwd){
        //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空！");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空！");
    }

    /**
     * 查询所有销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 添加用户操作
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        /*1、参数校验*/
        checkUserParams(user.getUserName(),user.getPhone(),user.getEmail(),null);

        /*2、设置参数默认值*/
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));

        /*3、执行添加操作，判断受影响的行数*/
        AssertUtil.isTrue(userMapper.insertSelective(user) != 1,"用户添加失败！");
    }

    /**
     * 更新用户操作
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        /*1、判断用户id是否存在*/
        AssertUtil.isTrue(user.getId() == null,"待更新记录不存在！");
        /*2、通过id查询用户记录*/
        User temp = userMapper.selectByPrimaryKey(user.getId());
        /*3、判断用户记录是否为空*/
        AssertUtil.isTrue(temp == null,"待更新记录不存在！");
        /*4、参数校验*/
        checkUserParams(user.getUserName(),user.getPhone(),user.getEmail(),user.getId());

        /*5、设置默认值*/
        user.setUpdateDate(new Date());
        /*6、执行更新操作，判断受影响行数*/
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1,"更新用户信息失败！");
    }

    /**
     * 参数校验
     * @param userName
     * @param phone
     * @param email
     */
    private void checkUserParams(String userName, String phone, String email, Integer userId) {
        //判断用户名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        //判断用户名是否唯一
        //通过用户名查询用户对象
        User temp = userMapper.queryUserByName(userName);
        //如果用户名为空，则表示用户名可用，如果用户名不为空，则表示用户名不可用
        AssertUtil.isTrue(temp != null && !temp.getId().equals(userId),"用户名已存在，请重新输入！");
        //判断邮箱是否为空
        AssertUtil.isTrue(email == null,"邮箱不能为空！");
        //判断手机号是否为空
        AssertUtil.isTrue(phone == null,"手机不能为空！");
        //判断手机号格式是否正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确！");
    }

    /**
     * 用户删除操作
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        //判断ids是否为空，长度是否大于0
        AssertUtil.isTrue(null == ids || ids.length == 0,"待删除记录不存在！");
        //执行删除操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length,"用户记录删除失败！");

    }
}

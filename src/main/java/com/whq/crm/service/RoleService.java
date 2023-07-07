package com.whq.crm.service;

import com.whq.crm.base.BaseService;
import com.whq.crm.dao.RoleMapper;
import com.whq.crm.utils.AssertUtil;
import com.whq.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询所有的角色列表
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        /*1、参数校验*/
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"参数名不能为空！");
        /*通过角色名查询角色记录*/
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        /*判断角色记录是否存在（添加操作时，如果角色记录存在则表示名称不可用）*/
        AssertUtil.isTrue(temp != null,"角色名已存在，请重新输入！");
        /*2、设置参数的默认值*/
        /*是否有效*/
        role.setIsValid(1);
        /*创建时间*/
        role.setCreateDate(new Date());
        /*更新时间*/
        role.setUpdateDate(new Date());
        /*3、执行添加操作，判断受影响的行数*/
        roleMapper.insertSelective(role);
    }
}

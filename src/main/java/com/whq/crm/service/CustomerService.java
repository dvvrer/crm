package com.whq.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whq.crm.base.BaseService;
import com.whq.crm.dao.CustomerMapper;
import com.whq.crm.query.CustomerQuery;
import com.whq.crm.query.SaleChanceQuery;
import com.whq.crm.utils.AssertUtil;
import com.whq.crm.vo.CusDevPlan;
import com.whq.crm.vo.Customer;
import com.whq.crm.vo.SaleChance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService extends BaseService<Customer,Integer> {

    @Resource
    private CustomerMapper customerMapper;

    /**
     * 多条件分页查询客户信息(返回的数据格式必须满足layui中数据表格要求的格式)
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        //得到对应的分页对象
        PageInfo<Customer> pageInfo = new PageInfo<>(customerMapper.selectByParams(customerQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 删除客户信息
     * @param id
     */
    public void deleteCustomer(Integer id) {
        //1、判断id是否为空，且数据存在
        AssertUtil.isTrue(null == id,"待删除的客户信息不存在！");
        //2、通过id查询计划项对象
        Customer customer = customerMapper.selectByPrimaryKey(id);
        //3、设置记录无效（删除）
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());
        //4、执行删除操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.deleteByPrimaryKey(id) != 1,"客户信息删除失败！");
    }

    /**
     * 更新客户信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer){
        /*1、参数校验*/
        //计划项ID     非空，数据存在
        AssertUtil.isTrue(null == customer.getId() || customerMapper.selectByPrimaryKey(customer.getId()) == null, "数据异常，请重试！");
        checkCusDevPlanParams(customer);

        /*2、设置默认参数*/
        //修改时间  系统当前时间
        customer.setUpdateDate(new Date());

        /*3、执行更新操作，判断受影响的行数*/
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) != 1,"客户信息更新失败！");
    }

    private void checkCusDevPlanParams(Customer customer) {
        //客户信息ID    非空，客户存在
        Integer id = customer.getId();
        AssertUtil.isTrue(id == null || customerMapper.selectByPrimaryKey(id) == null,"数据异常，请重试！");
        System.out.println(customer.getName());
        AssertUtil.isTrue(customer.getName() == null || customer.getName() == "","客户名称不能为空！");
        System.out.println(customer.getFr());
        AssertUtil.isTrue(customer.getFr() == null || customer.getFr() == "","法人不能为空！");
        System.out.println(customer.getArea());
        AssertUtil.isTrue(customer.getArea() == null || customer.getArea() == "","区域不能为空！");
        System.out.println(customer.getPhone());
        AssertUtil.isTrue(customer.getPhone() == null || customer.getPhone() == "","联系电话不能为空！");
    }
}

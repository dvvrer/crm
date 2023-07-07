package com.whq.crm.controller;

import com.whq.crm.base.BaseController;
import com.whq.crm.base.ResultInfo;
import com.whq.crm.query.CustomerQuery;
import com.whq.crm.service.CustomerService;
import com.whq.crm.vo.CusDevPlan;
import com.whq.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    /**
     * 客户信息管理数据查询（分页多条件查询）
     * @param customerQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }

    /**
     * 更新计划项
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        System.out.println(customer.getLevel());
        return success("客户信息更新成功！");
    }

    @RequestMapping("toAddOrUpdateCustomerPage")
    public String toAddOrUpdateCustomerPage(Integer id, HttpServletRequest request){
        Customer customer = customerService.selectByPrimaryKey(id);
        request.setAttribute("customer",customer);
        return "customer/add_update";
    }

    /**
     * 取下拉框选中的值
     * @param id
     * @return
     */
    @RequestMapping("selected")
    @ResponseBody
    public String selected(Integer id){
        Customer customer = customerService.selectByPrimaryKey(id);
        return customer.getLevel();
    }

    /**
     * 删除客户信息
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id){
        System.out.println(id);
        return success("客户信息删除成功！");
    }

}

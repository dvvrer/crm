package com.whq.crm.query;

import com.whq.crm.base.BaseQuery;

/**
 * 营销机会查询类
 */
public class SaleChanceQuery extends BaseQuery {

    //分类参数

    //条件参数
    private String customerName;    //客户名
    private String createMan;   //创建人
    private Integer state;  //分配状态  0=未分配   1=已分配

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}

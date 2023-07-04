package com.whq.crm.query;

import com.whq.crm.base.BaseQuery;

public class CusDevPlanQuery extends BaseQuery {

    private String saleChanceId;    //营销机会的主键

    public String getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(String saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}

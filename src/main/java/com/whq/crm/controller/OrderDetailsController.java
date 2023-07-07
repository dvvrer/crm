package com.whq.crm.controller;

import com.whq.crm.base.BaseController;
import com.whq.crm.query.OrderDetailsQuery;
import com.whq.crm.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@RequestMapping("order_details")
@Controller
public class OrderDetailsController extends BaseController {

    @Resource
    private OrderDetailsService orderDetailsService;


    /**
     * 分页条件查询订单详情列表
     *
     * @param orderDetailsQuery
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        return orderDetailsService.queryOrderDetailsByParams(orderDetailsQuery);
    }
}

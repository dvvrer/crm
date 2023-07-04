package com.whq.crm.controller;

import com.whq.crm.base.BaseController;
import com.whq.crm.base.ResultInfo;
import com.whq.crm.dao.CusDevPlanMapper;
import com.whq.crm.enums.StateStatus;
import com.whq.crm.query.CusDevPlanQuery;
import com.whq.crm.query.SaleChanceQuery;
import com.whq.crm.service.CusDevPlanService;
import com.whq.crm.service.SaleChanceService;
import com.whq.crm.utils.LoginUserUtil;
import com.whq.crm.vo.CusDevPlan;
import com.whq.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 进入客户开发计划页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){

        //通过id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //将对象设置到请求域中
        request.setAttribute("saleChance",saleChance);

        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 客户开发计划数据查询（分页多条件查询）
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(CusDevPlanQuery cusDevPlanQuery){

        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 添加计划项
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功！");
    }

    /**
     * 更新计划项
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功！");
    }

    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(HttpServletRequest request,Integer sId,Integer id){
        request.setAttribute("sId",sId);

        //通过计划项ID查询记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        //将计划项数据设置到请求域中
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }

    /**
     * 删除计划项
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项删除成功！");
    }


}

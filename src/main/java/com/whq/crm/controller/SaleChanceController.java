package com.whq.crm.controller;

import com.whq.crm.base.BaseController;
import com.whq.crm.base.ResultInfo;
import com.whq.crm.enums.StateStatus;
import com.whq.crm.query.SaleChanceQuery;
import com.whq.crm.service.SaleChanceService;
import com.whq.crm.utils.CookieUtil;
import com.whq.crm.utils.LoginUserUtil;
import com.whq.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 营销机会数据查询（分页多条件查询）
     * 如果flag的值不为空且为1，则表示当前查询的是客户开发计划；否则查询营销机会数据
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){
        //判断flag的值
        if (flag != null && flag == 1){
            //查询客户开发计划
            //设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            //设置指派人（当前登录用户的id）
            //从cookie中获取当前登录用户的id
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        //从cookie中获取当前登录的用户名
        String username = CookieUtil.getCookieValue(request,"userName");
        //设置用户到营销机会对象
        saleChance.setCreateMan(username);
        //调用Service层的添加方法
        saleChanceService.addSaleChance(saleChance);

        return success("营销机会数据添加成功！");
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        //调用Service层的添加方法
        saleChanceService.updateSaleChance(saleChance);

        return success("营销机会数据更新成功！");
    }

    /**
     * 进入添加/修改营销机会页面
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId,HttpServletRequest request){
        if (saleChanceId != null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance",saleChance);
        }

        return "saleChance/add_update";
    }

    /**
     * 删除营销机会
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        //调用service层的删除方法
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会删除成功");
    }

    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("营销机会状态更新成功！");
    }
}

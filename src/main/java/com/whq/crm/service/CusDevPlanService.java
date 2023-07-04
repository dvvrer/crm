package com.whq.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whq.crm.base.BaseService;
import com.whq.crm.dao.CusDevPlanMapper;
import com.whq.crm.dao.SaleChanceMapper;
import com.whq.crm.query.CusDevPlanQuery;
import com.whq.crm.query.SaleChanceQuery;
import com.whq.crm.utils.AssertUtil;
import com.whq.crm.vo.CusDevPlan;
import com.whq.crm.vo.SaleChance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询客户开发计划(返回的数据格式必须满足layui中数据表格要求的格式)
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        //得到对应的分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 添加客户开发项数据
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        //1、参数校验
        checkCusDevPlanParams(cusDevPlan);
        //2、设置参数的默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        //3、执行添加操作，判断受影响的行数
        Integer num = cusDevPlanMapper.insertSelective(cusDevPlan);
        AssertUtil.isTrue(num != 1,"计划项数据添加失败！");
    }

    /**
     * 更新客户开发数据项
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        /*1、参数校验*/
        //计划项ID     非空，数据存在
        AssertUtil.isTrue(null == cusDevPlan.getId() ||
                cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,
                "数据异常，请重试！");
        checkCusDevPlanParams(cusDevPlan);

        /*2、设置默认参数*/
        //修改时间  系统当前时间
        cusDevPlan.setUpdateDate(new Date());

        /*3、执行更新操作，判断受影响的行数*/
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"计划项更新失败！");
    }

    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //营销机会ID    非空，机会存在
        Integer sId = cusDevPlan.getSaleChanceId();
        System.out.println(sId);
        System.out.println(saleChanceMapper.selectByPrimaryKey(sId));
        AssertUtil.isTrue(sId == null || saleChanceMapper.selectByPrimaryKey(sId) == null,"数据异常，请重试！");
        //计划项内容 非空
        AssertUtil.isTrue(cusDevPlan.getPlanItem() == null,"计划项内容不能为空！");

        //计划时间  非空
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null,"计划时间不能为空！");
    }

    /**
     * 删除计划项
     * @param id
     */
    public void deleteCusDevPlan(Integer id) {
        //1、判断id是否为空，且数据存在
        AssertUtil.isTrue(null == id,"待删除的计划项不存在！");
        //2、通过id查询计划项对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        //3、设置记录无效（删除）
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        //4、执行删除操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.deleteByPrimaryKey(id) != 1,"计划项删除失败！");
    }
}

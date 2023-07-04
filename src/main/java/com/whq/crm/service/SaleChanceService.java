package com.whq.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whq.crm.base.BaseService;
import com.whq.crm.dao.SaleChanceMapper;
import com.whq.crm.enums.DevResult;
import com.whq.crm.enums.StateStatus;
import com.whq.crm.query.SaleChanceQuery;
import com.whq.crm.utils.AssertUtil;
import com.whq.crm.utils.PhoneUtil;
import com.whq.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会(返回的数据格式必须满足layui中数据表格要求的格式)
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //得到对应的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        /*1、参数校验*/
        AssertUtil.isTrue(null == saleChance.getId(),"待更新记录不存在！");
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null == temp,"待更新记录不存在！");
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        /*2、设置相关参数的默认值*/
        //设置更新时间，设置为系统当前时间
        saleChance.setUpdateDate(new Date());
        //设置指派人
        //判断原始数据是否存在
        if (StringUtils.isBlank(temp.getAssignMan())){    //不存在
            //判断修改后的值是否存在
            if (StringUtils.isBlank(saleChance.getAssignMan())){    //修改前为空，修改后有值
                //设置指派时间，为当前系统时间
                saleChance.setAssignTime(new Date());
                //分配状态
                saleChance.setState(StateStatus.STATED.getType());
                //开发状态
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        }else { //存在
            //修改后的值是否存在
            if (StringUtils.isBlank(saleChance.getAssignMan())){    //修改前有值，修改后无值
                saleChance.setCreateMan(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else { //修改前有值，修改后有值
                if (!temp.getAssignMan().equals(saleChance.getAssignMan())){
                    //更新指派时间
                    saleChance.setAssignTime(new Date());
                }else {
                    //设置指派时间为修改前的指派时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }

        /*3、执行更新操作，判断影响的行数*/
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"更新营销机会失败！");

    }

    /**
     * 添加营销机会
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){

        //校验参数
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置相关字段的默认值
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //判断是否设置了相关的指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())){
            //如果为空表示未设置
            saleChance.setState(StateStatus.UNSTATE.getType());
            //没有设置指派人，所以指派时间为空
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else{
            //如果不为空表示已设置指派人
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }

        //执行添加操作
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1,"添加营销机会失败！");

    }

    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        //判断客户名字是否为空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名字为空！");
        //判断联系人是否为空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人为空!");
        //判断联系号码是否为空，格式是否正确
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"手机号码为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号码格式不正确！");
    }

    /**
     * 删除营销机会
     * @param ids
     */
    @Transactional
    public void deleteSaleChance(Integer[] ids){
        //判断id是否为空
        AssertUtil.isTrue(ids == null || ids.length < 0,"待删除记录不存在！");
        //执行删除操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length,"营销机会数据删除失败！");

    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     */
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        //1、判断id是否非空
        AssertUtil.isTrue(null == id,"待更新记录不存在！");
        //2、通过id查询营销机会数据
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //3、判断对象是否为空
        AssertUtil.isTrue(saleChance == null,"待更新记录不存在！");
        //4、设置开发状态
        saleChance.setDevResult(devResult);
        //5、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"营销开发机会状态更新失败！");
    }
}

layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载计划项数据表格
     */
    var tableIns = table.render({
        id:'cusDevPlanTable'
        ,elem:'#cusDevPlanList'
        //容器的高度 full-差值
        ,height:'full-125'
        //单元格最小宽度
        ,cellMinWidth:95
        //访问数据的url（后台的数据接口）
        ,url:ctx + '/cus_dev_plan/list?saleChanceId=' + $("[name = 'id']").val() //数据接口
        ,page: true  //分页开启
        //每页默认显示的数量
        ,limit:10
        ,limits:[10,20,30,40,50]
        //开启头部工具栏
        ,toolbar:'#toolbarDemo'
        ,cols:[[ //表头
            //field：要求field属性值与返回的数据中对应的属性字段名一致
            {type:'checkbox',fixed: 'center'}
            ,{field:'id',title:'编号',width:80,sort:true, fixed: 'left'}
            ,{field:'planItem',title:'计划项',align:'center'}
            ,{field:'planDate',title:'计划时间',align:'center'}
            ,{field:'exeAffect',title:'执行效果',align:'center'}
            ,{field:'createDate',title:'创建时间',align:'center'}
            ,{field:'updateDate',title:'修改时间',align:'center'}
            ,{title: '操作',templet:'#cusDevPlanListBar',fixed: 'right',align:'center',minWidth:150}
        ]]
    });

    /**
     * 打开添加或修改计划项的页面
     */
    function openAddOrUpdateCusDevPlanDialog(id) {
        var title = "计划项管理 - 添加计划项"
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name = 'id']").val();

        //判断计划项id是否为空（如果为空，则表示添加，不为空，则表示更新操作）
        if (id != null && id != ''){    //更新操作
            title = "计划项管理 - 更新计划项";
            url += "&id=" + id;
        }


        layui.layer.open({
            //类型
            type: 2,
            //标题
            title: title,
            //宽高
            area: ['500px','300px'],
            //url地址
            content: url,
            //最大最小化
            maxmin: true
        });
    }

    /**
     * 更新营销机会的开发状态
     * @param number
     */
    function updateSaleChanceDevResult(devResult) {
        //弹出询问框，询问用户是否确认删除
        layer.confirm("您确认要更改状态吗？",{icon: 3},function (index){
            //得到需要被更新的营销机会id（通过隐藏域获取）
            var sId = $("[name = 'id']").val();
            //发送ajax请求，更新营销机会的开发状态
            $.post(ctx + "/sale_chance/updateSaleChanceDevResult",{id:sId,devResult:devResult},function (result){
                if (result.code == 200){
                    layer.msg("更新成功！",{icon: 6});
                    //关闭窗口
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }else {
                    layer.msg(result.msg,{icon: 5});
                }
            });
        });
    }

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(cusDevPlans)',function (data){
        if (data.event == "add"){   //添加计划项
            //打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog();

        }else if (data.event == "success"){ //开发成功
            //更新营销机会的开发状态
            updateSaleChanceDevResult(2);
        }else if (data.event == "failed"){  //开发失败
            //更新营销机会的开发状态
            updateSaleChanceDevResult(3);
        }
    });

    /**
     * 删除计划项
     * @param id
     */
    function deleteCusDevPlan(id) {
        //弹出确认框，询问用户是否确认删除
        layer.confirm('您确认要删除该条记录吗？',{icon: 3,title: '开发项数据管理'},function (index){
            //发送ajax请求，执行删除操作
            $.post(ctx + "/cus_dev_plan/delete",{id:id},function (result){
                //执行删除结果
                if (result.code == 200){
                    //删除成功
                    layer.msg("删除成功",{icon: 6})
                    //刷新数据表格
                    tableIns.reload();
                }else {
                    //删除失败，给出提示信息
                    layer.msg(result.msg,{icon: 5})
                }
            });
        });
    }

    /**
     * 监听行工具栏
     */
    table.on('tool(cusDevPlans)',function (data){
        if (data.event == "edit"){  //更新计划项
            //打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog(data.data.id);
        }else if (data.event == "del"){ //删除计划项
            deleteCusDevPlan(data.data.id);
        }
    });


});

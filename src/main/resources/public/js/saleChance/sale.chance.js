layui.use(['table','layer'],function (){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table

    /**
     * 加载数据表格
     */
    var tableIns = table.render({
        id:'saleChanceTable'
       ,elem:'#saleChanceList'
        //容器的高度 full-差值
       ,height:'full-125'
        //单元格最小宽度
        ,cellMinWidth:95
        //访问数据的url（后台的数据接口）
       ,url:ctx + '/sale_chance/list' //数据接口
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
            ,{field:'chanceSource',title:'机会来源',align:'center'}
            ,{field:'customerName',title:'客户名称',align:'center'}
            ,{field:'cgjl',title:'成功几率',align:'center'}
            ,{field:'overview',title:'概要',align:'center',width: 120}
            ,{field:'linkMan',title:'联系人',align:'center'}
            ,{field:'linkPhone',title:'联系号码',align:'center',width: 120}
            ,{field:'description',title:'描述',align:'center',width: 120}
            ,{field:'createMan',title:'创建人',align:'center'}
            ,{field:'uname',title:'分配人',align:'center'}
            ,{field:'createDate',title:'创建时间',align:'center',width: 170}
            ,{field:'updateTime',title:'修改时间',align:'center',width: 170}
            ,{field:'state',title:'分配状态',align:'center',templet: function (d){
                //调用函数，返回格式化的结果
                return formatState(d.state);
                }}
            ,{field:'devResult',title:'开发状态',align:'center',templet: function (d){
                //调用函数，返回格式化的结果
                return formatDevResult(d.devResult);
                }}
            ,{title: '操作',templet:'#saleChanceListBar',fixed: 'right',align:'center',minWidth:150}
        ]]
    });

    /**
     * 格式化状态值
     * 0=未分配
     * 1=已分配
     * 其他=未知
     */
    function formatState(state){
        if (state == 0){
            return "<div style='color: yellow'>未分配</div>";
        }else if (state == 1){
            return "<div style='color: green'>已分配</div>";
        }else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态0=未分配
     * 0=未开发
     * 1=开发中
     * 2=开发成功
     * 3=开发失败
     * 其他=未知
     * @param devResult
     */
    function formatDevResult(devResult){
        if (devResult == 0){
            return "<div style='color: yellow'>未开发</div>";
        }else if (devResult == 1){
            return "<div style='color: orange'>开发中</div>";
        }else if (devResult == 2){
            return "<div style='color: green'>开发成功</div>";
        }else if (devResult == 3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: blue'>未知</div>";
        }
    }

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function (){
        /**
         * 表格重载
         *  多条件查询
         */
        tableIns.reload({
            //设置需要传递给后端的参数
            where:{
                //通过文本框的值设置传递的参数
                customerName:$("[name='customerName']").val() //客户名称
                ,createMan:$("[name='createMan']").val()   //创建人
                ,state:$("#state").val()       //状态
            }
            ,page:{
                curr:1//重新从第1页开始
            }
        });
    });


    /**
     * 删除营销机会
     * @param data
     */
    function deleteSaleChance(data) {
        //获取数据表格中选中的行数据
        var checkStatus = table.checkStatus("saleChanceTable");
        console.log(checkStatus);
        //获取所有被选中的记录对应的数据
        var saleChanceData = checkStatus.data;
        //判断是否选择了要删除的记录
        if (saleChanceData.length < 1){
            layer.msg("请选择要删除的记录！",{icon: 5});
            return;
        }
        //询问用户是否确认删除
        layer.confirm("您确认要删除选中的记录吗？",{icon: 3,title: '营销机会管理'},function (index){
            //关闭确认框
            layer.close(index)
            var ids = "";
            //循环选中的行记录
            for (let i = 0; i < saleChanceData.length; i++) {
                ids = ids + "&ids=" + saleChanceData[i].id
            }
            // console.log(ids);

            //发送ajax请求，执行删除营销机会
            $.ajax({
                type: "post",
                url: ctx + "/sale_chance/delete",
                data:ids,
                success: function (result){
                    //判断删除结果
                    if (result.code == 200){
                        //提示成功
                        layer.msg("删除成功！",{icon: 6})
                        //重载表格
                        tableIns.reload();
                    }else {
                        //提示失败
                        layer.msg(result.msg,{icon: 5})
                    }
                }
            });
        });
    }

    table.on('toolbar(saleChances)',function (data){
        console.log(data);
        if (data.event == "add"){
            //添加操作
            openSaleChanceDialog();

        }else if (data.event == "del"){
            //删除操作
            deleteSaleChance(data);
        }
    })

    /**
     * 打开添加/修改营销机会数据的窗口
     * 如果saleChanceId为空则为添加操作
     * 如果saleChanceId不为空则为修改操作
     */
    function openSaleChanceDialog(saleChanceId){
        //弹出层的标题
        var title = "<h2>营销机会管理-添加营销机会</h2>";
        var url = ctx + "/sale_chance/toSaleChancePage";

        //判断saleChanceId是否为空
        if (saleChanceId != null && saleChanceId != ''){
            //修改操作
            title = "<h2>营销机会管理-更新营销机会</h2>";
            //请求地址添加营销机会id
            url += "?saleChanceId=" + saleChanceId;
        }
        layui.layer.open({
            //类型
            type: 2,
            //标题
            title: title,
            //宽高
            area: ['500px','620px'],
            //url地址
            content: url,
            //最大最小化
            maxmin: true
        });
    }

    table.on('tool(saleChances)',function(data){
        if (data.event == "edit"){  //编辑操作
            var saleChanceId = data.data.id;
            //打开修改营销机会窗口
            openSaleChanceDialog(saleChanceId);
        }else if (data.event == "del"){  //删除操作
            //弹出询问框，询问用户是否确认删除
            layer.confirm("确认要删除吗？",{icon: 3,title: "营销机会管理"},function (index){
                //关闭确认框
                layer.close(index);
                //发送对应的ajax请求，删除记录
                $.ajax({
                    type: "post",
                    url: ctx + "/sale_chance/delete",
                    data:{
                        ids:data.data.id
                    },
                    success:function (result){
                        //判断删除结果
                        if (result.code == 200){
                            //提示成功
                            layer.msg("删除成功！",{icon: 6})
                            //重载表格
                            tableIns.reload();
                        }else {
                            //提示失败
                            layer.msg(result.msg,{icon: 5})
                        }
                    }
                });
            });
        }
    });


});
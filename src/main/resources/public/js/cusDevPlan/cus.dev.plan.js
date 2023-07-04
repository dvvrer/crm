layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

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
        //访问数据的url（后台的数据接口） 传递flag参数，表示查询的是客户开发计划数据
        ,url:ctx + '/sale_chance/list?flag=1' //数据接口
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
            ,{title: '操作',templet:'#op',fixed: 'right',align:'center',minWidth:150}
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
                ,devResult:$("[name='devResult']").val()    //开发状态
            }
            ,page:{
                curr:1//重新从第1页开始
            }
        })
    })

    /**
     * 打开计划项开发或详情页面
     * @param title
     * @param id
     */
    function openCusDevPlanDialog(title, id) {

        layui.layer.open({
            //类型
            type: 2,
            //标题
            title: title,
            //宽高
            area: ['750px','550px'],
            //url地址
            content: ctx + "/cus_dev_plan/toCusDevPlanPage?id="+id,
            //最大最小化
            maxmin: true
        });
    }

    /**
     * 行工具栏监听
     */
    table.on('tool(saleChances)',function (data){
       //判断类型
        if (data.event == "dev"){   //开发
            //打开计划项开发与详情页面
            openCusDevPlanDialog("计划项数据开发",data.data.id);

        }else if (data.event == "info"){    //详情
            //打开计划项数据维护页面
            openCusDevPlanDialog("计划项数据维护",data.data.id);
        }
    });
});

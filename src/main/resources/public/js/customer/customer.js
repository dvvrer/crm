layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form;

    //客户列表展示
    var tableIns = table.render({
        elem: '#customerList',
        url : ctx+'/customer/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "customerListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'name', title: '客户名',align:"center"},
            {field: 'fr', title: '法人',  align:'center'},
            {field: 'khno', title: '客户编号', align:'center'},
            {field: 'area', title: '地区', align:'center'},
            {field: 'cusManager', title: '客户经理',  align:'center'},
            {field: 'myd', title: '满意度', align:'center'},
            {field: 'level', title: '客户级别', align:'center'},
            {field: 'xyd', title: '信用度', align:'center'},
            {field: 'address', title: '详细地址', align:'center'},
            {field: 'postCode', title: '邮编', align:'center'},
            {field: 'phone', title: '电话', align:'center'},
            {field: 'webSite', title: '网站', align:'center'},
            {field: 'fax', title: '传真', align:'center'},
            {field: 'zczj', title: '注册资金', align:'center'},
            {field: 'yyzzzch', title: '营业执照', align:'center'},
            {field: 'khyh', title: '开户行', align:'center'},
            {field: 'khzh', title: '开户账号', align:'center'},
            {field: 'gsdjh', title: '国税', align:'center'},
            {field: 'dsdjh', title: '地税', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '更新时间', align:'center'},
            {title: '操作', templet:'#customerListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

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
                customerName:$("[name='name']").val() //客户名称
                ,customerNo:$("[name='khno']").val() //客户编号
                ,level:$("[name='level']").val() //客户级别
            }
            ,page:{
                curr:1//重新从第1页开始
            }
        });
    });

    function openAddOrUpdateCustomerDialog(id) {
        var title = "客户信息管理 - 更新操作";
        var url = ctx + "/customer/toAddOrUpdateCustomerPage?id=" + id;
        //iframe层
        layui.layer.open({
            //类型
            type: 2,
            //标题
            title: title,
            //宽高
            area: ['650px','400px'],
            //url地址
            content: url,
            //最大最小化
            maxmin: true
        });
    }

    /**
     * 删除单条客户信息
     * @param id
     */
    function deleteCustomer(id) {
        //弹出询问框，询问用户是否确认删除
        layer.confirm("确认要删除吗？",{icon: 3,title: "客户信息管理管理"},function (index){
            var url = ctx + "/customer/delete?id=" + id;
            //关闭确认框
            layer.close(index);
            //发送对应的ajax请求，删除记录
            $.ajax({
                type: "post",
                url: url,
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

    table.on('tool(customers)',function (data){
        if (data.event == "edit"){   //编辑客户信息
            //打开添加/修改用户对话框
            openAddOrUpdateCustomerDialog(data.data.id);
        }else if (data.event == "del"){
            deleteCustomer(data.data.id);
        }
    });

});

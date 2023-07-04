layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var tableIns = table.render({
        id:'userTable'
        ,elem:'#userList'
        //容器的高度 full-差值
        ,height:'full-125'
        //单元格最小宽度
        ,cellMinWidth:95
        //访问数据的url（后台的数据接口）
        ,url:ctx + '/user/list' //数据接口
        ,page: true  //分页开启
        //每页默认显示的数量
        ,limit:10
        ,limits:[10,20,30,40,50]
        //开启头部工具栏
        ,toolbar:'#toolbarDemo'
        ,cols:[[ //表头
            //field：要求field属性值与返回的数据中对应的属性字段名一致
            {type:'checkbox',fixed: 'center'}
            ,{field:'id',title:'编号',width:80,sort:true, fixed: 'left',align: "center"}
            ,{field:'userName',title:'用户名称',align:'center'}
            ,{field:'trueName',title:'真是姓名',align:'center'}
            ,{field:'email',title:'用户邮箱',align:'center'}
            ,{field:'phone',title:'用户号码',align:'center'}
            ,{field:'createDate',title:'创建时间',width:170,align:'center'}
            ,{field:'updateDate',title:'修改时间',width:170,align:'center'}
            ,{title: '操作',templet:'#userListBar',fixed: 'right',align:'center',minWidth:150}
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
                userName:$("[name='userName']").val() //用户名称
                ,email:$("[name='email']").val()   //邮箱
                ,phone:$("[name='phone']").val()       //手机号码
            }
            ,page:{
                curr:1//重新从第1页开始
            }
        });
    });

    function openAddOrUpdateUserDialog(id) {
        var title = "用户管理 - 添加操作";
        var url = ctx + "/user/toAddOrUpdateUserPage";
        //判断id是否为空，如果为空，则为添加操作，否则是修改操作
        if (id != null && id != ''){
            title = "用户管理 - 修改操作";
            url += "?id=" + id; //传递主键，查询数据
        }
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

    function deleteUsers(userData) {
        //判断用户是否选择了要删除的记录
        if (userData.length == 0){
            layer.msg("请选择要删除的记录！")
            return;
        }

        //询问用户是否确认删除
        layer.confirm("您确认要删除选中的记录吗？",{icon: 3,title: '用户管理'},function (index){
            //关闭确认框
            layer.close(index)
            var ids = "ids=";
            //循环选中的行记录
            for (let i = 0; i < userData.length; i++) {
                if (i < userData.length - 1){
                    ids = ids + userData[i].id + "&ids="
                }else {
                    ids = ids + userData[i].id;
                }
            }

            //发送ajax请求，执行删除用户机会
            $.ajax({
                type: "post",
                url: ctx + "/user/delete",
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
        })
    }

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(users)',function (data){
        if (data.event == "add"){   //添加用户
            //打开添加/修改用户对话框
            openAddOrUpdateUserDialog();

        }else if (data.event == "del"){ //删除用户
            //获取被选中的数据的信息
            var checkStatus = table.checkStatus(data.config.id)
            //删除多个用户记录
            deleteUsers(checkStatus.data);
        }
    });

    /**
     * 删除单条用户记录
     * @param id
     */
    function deleteUser(id) {
        //弹出询问框，询问用户是否确认删除
        layer.confirm("确认要删除吗？",{icon: 3,title: "用户管理"},function (index){
            //关闭确认框
            layer.close(index);
            //发送对应的ajax请求，删除记录
            $.ajax({
                type: "post",
                url: ctx + "/user/delete",
                data:{
                    ids:id
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

    /**
     * 监听行工具栏
     */
    table.on('tool(users)',function (data){
        if (data.event == "edit"){   //添加用户
            //打开添加/修改用户对话框
            openAddOrUpdateUserDialog(data.data.id);
        }else if (data.event == "del"){
            console.log(data.data.id)
            deleteUser(data.data.id);
        }
    });
});
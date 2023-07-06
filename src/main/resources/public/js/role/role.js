layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

       /**
        * 加载数据表格
        */
       var tableIns = table.render({
              id:'roleTable'
              ,elem:'#roleList'
              //容器的高度 full-差值
              ,height:'full-125'
              //单元格最小宽度
              ,cellMinWidth:95
              //访问数据的url（后台的数据接口）
              ,url:ctx + '/role/list' //数据接口
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
                     ,{field:'roleName',title:'角色名称',align:'center'}
                     ,{field:'roleRemark',title:'角色备注',align:'center'}
                     ,{field:'createDate',title:'创建时间',width:170,align:'center'}
                     ,{field:'updateDate',title:'修改时间',width:170,align:'center'}
                     ,{title: '操作',templet:'#roleListBar',fixed: 'right',align:'center',minWidth:150}
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
                            roleName:$("[name='roleName']").val() //角色名称
                     }
                     ,page:{
                            curr:1//重新从第1页开始
                     }
              });
       });

       /**
        * //打开添加/更新角色对话框
        */
       function openAddOrUpdateDialog() {
              var title = "角色管理-角色添加";
              var url = ctx + "/role/toAddOrUpdateRolePage";

              layui.layer.open({
                     title: title,
                     content: url,
                     area: ["430px","300px"],
                     type: 2,
                     maxmin: true
              });
       }

       table.on('toolbar(roles)',function (data){
              //判断lay-event属性
              if (data.event == "add"){   //添加操作
                     //打开添加/更新角色对话框
                     openAddOrUpdateDialog();
              }
       });

});

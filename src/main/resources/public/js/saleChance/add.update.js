layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 监听submit事件
     */
    form.on('submit(addOrUpdateSaleChance)',function (data){
        //提交数据时的加载层
        var index = layer.msg("数据提交中，请稍后...",{
            icon: 16,   //图标
            time: false,
            shade: 0.8
        });

        //发送ajax请求
        var url = ctx + "/sale_chance/add";

        //通过营销机会id来判断当前需要执行添加操作还是修改操作
        //如果营销机会id为空，则需要进行添加操作，如果营销机会id不为空，则需要进行修改操作
        var saleChanceId = $("[name = 'id']").val();
        //判断saleChanceId是否为空
        if (saleChanceId != null && saleChanceId != ''){
            url = ctx + "/sale_chance/update";
        }

        $.post(url,data.field,function (result){
            //判断执行是否成功，200=成功
            if (result.code == 200){
                //成功
                //提示成功
                layer.msg("操作成功！",{icon:6})
                //关闭加载层
                layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //刷新父窗口，重新加载数据
                parent.location.reload();
            }else {
                //失败
                layer.msg(result.msg,{icon:5});
            }
        });

        //阻止表单提交
        return false;
    })

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })

    /*加载指派人的下拉框*/
    $.ajax({
        type: "get",
        url: ctx + "/user/queryAllSales",
        data: {},
        success: function (data){
            // console.log(data);
            //判断返回的数据是否为空
            if (data != null){
                //获取隐藏域中指派人ID
                var assignManId = $("#assignManId").val();
                //遍历返回的数据
                for (let i = 0; i < data.length; i++) {
                    var opt = "";
                    //如果循环得到的指派人ID与隐藏域中的ID相等，则表示被选中
                    if (assignManId == data[i].id){
                        //设置下拉框选项 设置下拉框选项被选中
                        opt = "<option value='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                    }else {
                        //设置下拉框选项
                        opt = "<option value='"+data[i].id+"'>"+data[i].uname+"</option>";
                    }

                    //将下拉项设置到下拉框中
                    $("#assignMan").append(opt);
                }
            }
            //重新渲染下拉框的内容
            layui.form.render("select");
        }

    });

});
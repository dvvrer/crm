layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 表单submit监听
     */
    form.on('submit(addOrUpdateCusDevPlan)',function (data){
        //提交数据时的加载层
        var index = top.layer.msg("数据提交中，请稍后...",{
            icon: 16,   //图标
            time: false,
            shade: 0.8
        });

        //得到表单所有的数据
        var formData = data.field;
        console.log(data.field)
        //请求地址
        var url = ctx + "/cus_dev_plan/add";

        //判断计划项id是否为空（如果不为空，则表示更新）
        if ($('[name = "id"]').val()){
            url = ctx + "/cus_dev_plan/update";
        }

        $.post(url,formData,function (result){
            //判断执行是否成功，200=成功
            if (result.code == 200){
                //成功
                //提示成功
                top.layer.msg("操作成功！",{icon:6})
                //关闭加载层
                top.layer.close(index);
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
    });

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })

});
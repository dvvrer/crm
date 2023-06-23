package com.whq.crm.controller;

import com.whq.crm.base.BaseController;
import com.whq.crm.base.ResultInfo;
import com.whq.crm.exceptions.ParamsException;
import com.whq.crm.model.UserModel;
import com.whq.crm.service.UserService;
import com.whq.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){

        ResultInfo resultInfo = new ResultInfo();

        //通过try catch捕获service层的异常，如果service层抛出异常，则表示登录失败，否则登录失败
        try{
            //调用service层登录方法
            UserModel userModel = userService.userLogin(userName,userPwd);

            //设置resultInfo的result的值（将数据返回给请求）
            resultInfo.setResult(userModel);

        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("登录失败！");
        }

        return resultInfo;
    }

    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,
                                         String oldPassword,
                                         String newPassword,
                                         String repeatPassword){
        ResultInfo resultInfo = new ResultInfo();

        try{
            //获取cookie中的userId
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //调用Service层的修改密码方法
           userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("修改密码失败！");
            e.printStackTrace();
        }

        return resultInfo;
    }
}

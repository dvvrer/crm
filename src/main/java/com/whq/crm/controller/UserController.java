package com.whq.crm.controller;

import com.whq.crm.base.BaseController;
import com.whq.crm.base.ResultInfo;
import com.whq.crm.exceptions.ParamsException;
import com.whq.crm.model.UserModel;
import com.whq.crm.query.UserQuery;
import com.whq.crm.service.UserService;
import com.whq.crm.utils.LoginUserUtil;
import com.whq.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){

        ResultInfo resultInfo = new ResultInfo();

        //调用service层登录方法
        UserModel userModel = userService.userLogin(userName,userPwd);

        //设置resultInfo的result的值（将数据返回给请求）
        resultInfo.setResult(userModel);

//        //通过try catch捕获service层的异常，如果service层抛出异常，则表示登录失败，否则登录失败
//        try{
//            //调用service层登录方法
//            UserModel userModel = userService.userLogin(userName,userPwd);
//
//            //设置resultInfo的result的值（将数据返回给请求）
//            resultInfo.setResult(userModel);
//
//        }catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败！");
//        }

        return resultInfo;
    }

    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,
                                         String oldPassword,
                                         String newPassword,
                                         String repeatPassword){
        ResultInfo resultInfo = new ResultInfo();

        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用Service层的修改密码方法
        userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);

//        try{
//            //获取cookie中的userId
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            //调用Service层的修改密码方法
//            userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);
//        }catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败！");
//            e.printStackTrace();
//        }

        return resultInfo;
    }

    //进入修改用户密码的页面
    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }


    @ResponseBody
    @RequestMapping("queryAllSales")
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户信息
     * @param user
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功！");
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功！");
    }


    /**
     * 打开添加或修改用户页面
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id,HttpServletRequest request){
        //判断id是否为空，不为空则表示更新操作，查询用户对象
        if (id != null){
            //查询用户对象
            User user = userService.selectByPrimaryKey(id);
            System.out.println(user.getUserName());
            System.out.println(user.getTrueName());
            System.out.println(user.getEmail());
            System.out.println(user.getPhone());
            //将数据设置到请求域中
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByIds(ids);

        return success("用户记录删除成功！");
    }
}

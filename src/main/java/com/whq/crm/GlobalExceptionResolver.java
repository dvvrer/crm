package com.whq.crm;

import com.alibaba.fastjson.JSON;
import com.whq.crm.base.ResultInfo;
import com.whq.crm.exceptions.NoLoginException;
import com.whq.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     * 异常处理方法
     * 方法的返回值：
     *  1、返回视图
     *  2、返回数据（JSON数据）
     *
     *  如何判断方法的返回值？
     *      通过方法上是否生命@ResponseBody注解
     *          如果未声明，则表示返回视图
     *          如果声明了，则表示返回数据
     * @param httpServletRequest    request请求对象
     * @param httpServletResponse   response响应对象
     * @param o 方法对象
     * @param e 异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        /**
         * 非法请求拦截
         *  判断是否抛出未登录异常
         *      如果抛出该异常，则要求用户登录，重定向跳转到登录页面
         */
        if (e instanceof NoLoginException){
            //重定向到登录页面
            ModelAndView modelAndView = new ModelAndView("redirect:/index");
            return modelAndView;
        }


        /*
            设置默认的异常处理（返回视图）
         */
        ModelAndView modelAndView = new ModelAndView("error");
        //设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请重试。。。");

        //判断HandlerMethod
        if (o instanceof HandlerMethod){
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) o;
            //获取方法上声明的@ResponseBody注解对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断ResponseBody对象是否为空（如果对象为空，则表示返回的是视图；如果不为空，则表示返回的是数据）
            if (responseBody == null){
                /*
                方法返回视图
                 */
                //判断异常类型
                if (e instanceof ParamsException){
                    ParamsException p = (ParamsException) e;
                    //设置异常信息
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }
                return modelAndView;
            }else {
                /*
                方法返回数据
                 */
                //设置默认的异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试！");

                //判断异常类型是不是自定义异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                //设置类型及编码格式(响应json格式的数据)
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                //得到字符输出流
                PrintWriter out = null;
                try{
                    out = httpServletResponse.getWriter();
                    //将需要返回的对象转换成json格式的字符
                    String json = JSON.toJSONString(resultInfo);
                    //输出数据
                    out.write(json);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    //如果对象不为空，则关闭
                    if (out != null){
                        out.close();
                    }
                }
                return null;
            }
        }
        return modelAndView;
    }
}

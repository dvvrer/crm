package com.whq.crm.controller;

import com.whq.crm.base.BaseController;
import com.whq.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;
}

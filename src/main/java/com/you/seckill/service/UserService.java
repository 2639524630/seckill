package com.you.seckill.service;

import com.you.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.you.seckill.vo.LoginVo;
import com.you.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author 86152
* @description 针对表【t_user】的数据库操作Service
* @createDate 2023-05-01 20:45:33
*/
public interface UserService extends IService<User> {
    /**
     * @author ysj
     *
     * @param loginVo
     * @param request
     * @param response
     * @return java.lang.String
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    /**
     * @author ysj
     *
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return com.you.seckill.vo.RespBean
     */
    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}

package com.you.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.seckill.exception.GlobalException;
import com.you.seckill.pojo.User;
import com.you.seckill.service.UserService;
import com.you.seckill.mapper.UserMapper;
import com.you.seckill.utils.CookieUtil;
import com.you.seckill.utils.MD5Util;
import com.you.seckill.utils.UUIDUtil;
import com.you.seckill.vo.LoginVo;
import com.you.seckill.vo.RespBean;
import com.you.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author 86152
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2023-05-01 20:45:33
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @author ysj
     *
     * @param loginVo
     * @param request
     * @param response
     * @return java.lang.String
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
 //       log.info("密码：{}",password);
   /*     if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if (!ValidatorUtil.isMobile(mobile)){
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }*/


        User user = userMapper.selectById(mobile);
        if (user==null){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
           throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
//        判断密码是否正确
        if (!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
//      生成cookie
        String ticket= UUIDUtil.uuid();
//        将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket,user);
//        request.getSession().setAttribute(ticket,user);

        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success();
    }


    /**
     * @author ysj
     *    根据ticket获取user
     * @param userTicket
     * @return com.you.seckill.pojo.User
     */
    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,
                                HttpServletResponse response) {

        if (StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:"+userTicket);
        if (user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;


    }
    /**
     * @author ysj
     *
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return com.you.seckill.vo.RespBean
     */
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if (user==null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if (result==1){
//            操作成功,删除redis
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        else {
            return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
        }

    }
}





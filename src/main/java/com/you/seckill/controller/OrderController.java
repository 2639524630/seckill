package com.you.seckill.controller;


import com.you.seckill.pojo.User;
import com.you.seckill.service.OrderService;
import com.you.seckill.vo.OrderDetailVo;
import com.you.seckill.vo.RespBean;
import com.you.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping("/order")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail =orderService.detail(orderId);
      log.info("orderDetailVo:{}",detail.getOrder().toString());
       return RespBean.success(detail);

    }


}

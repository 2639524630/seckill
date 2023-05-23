package com.you.seckill.service;

import com.you.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.you.seckill.pojo.User;
import com.you.seckill.vo.GoodsVo;
import com.you.seckill.vo.OrderDetailVo;

/**
* @author 86152
* @description 针对表【t_order】的数据库操作Service
* @createDate 2023-05-02 19:49:27
*/
public interface OrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}

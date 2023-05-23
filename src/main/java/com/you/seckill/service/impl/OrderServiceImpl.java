package com.you.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.seckill.exception.GlobalException;
import com.you.seckill.pojo.Order;
import com.you.seckill.pojo.SeckillGoods;
import com.you.seckill.pojo.SeckillOrder;
import com.you.seckill.pojo.User;
import com.you.seckill.service.GoodsService;
import com.you.seckill.service.OrderService;
import com.you.seckill.mapper.OrderMapper;
import com.you.seckill.service.SeckillGoodsService;
import com.you.seckill.service.SeckillOrderService;
import com.you.seckill.vo.GoodsVo;
import com.you.seckill.vo.OrderDetailVo;
import com.you.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author 86152
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2023-05-02 19:49:27
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{
     @Autowired
     private SeckillGoodsService seckillGoodsService;

     @Autowired
     private OrderMapper orderMapper;

     @Autowired
     private SeckillOrderService seckillOrderService;

    /**
     * @author ysj
     *
     * @param user
     * @param goods
     * @return com.you.seckill.pojo.Order

     * * */
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                .eq("goods_id", goods.getId()));
        seckillGoods.setStoclCount(seckillGoods.getStoclCount()-1);
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().
                setSql("stocl_count=stocl_count-1").eq("goods_id", goods.getId())
                .gt("stocl_count", 0));
        if (!result){
            return null;
        }
//          生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setGoodsCount(1);
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDatatime(new Date());
        orderMapper.insert(order);
//          生成秒杀订单
        SeckillOrder seckillOrder1 = new SeckillOrder();
        seckillOrder1.setUserId(user.getId());
        seckillOrder1.setGoodsId(goods.getId());
        seckillOrder1.setOrderId(order.getId());
        seckillOrderService.save(seckillOrder1);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder1);
        return order;

    }

     @Autowired
     private GoodsService goodsService;
    @Autowired
     private RedisTemplate redisTemplate;
    /**
     * @author ysj
     *
     * @param orderId
     * @return com.you.seckill.vo.OrderDetailVo
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
       if (orderId==null){
           throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
       }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(order.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goodsVo);

        return orderDetailVo;
    }
}





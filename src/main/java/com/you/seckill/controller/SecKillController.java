package com.you.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import com.you.seckill.exception.GlobalException;
import com.you.seckill.pojo.Order;
import com.you.seckill.pojo.SeckillOrder;
import com.you.seckill.pojo.User;
import com.you.seckill.service.GoodsService;
import com.you.seckill.service.OrderService;
import com.you.seckill.service.SeckillOrderService;
import com.you.seckill.vo.GoodsVo;
import com.you.seckill.vo.RespBean;
import com.you.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/*
*
*   秒杀
*
* */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SecKillController  {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @author ysj
     *    秒杀页面跳转
     * @param model
     * @param user
     * @param goodsId
     * @return java.lang.String
     */
    @PostMapping("/doSeckill2")
   public String doSecKill2(Model model, User user,Long goodsId){
        if (user==null){
            return "login";
        }
      model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodVoByGoodsId(goodsId);
        log.info("user:{}",user);
        log.info("goods:{}",goods);
        if (goods.getStoclCount()<1) {
//            判断库存
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "seckillFail";
        }
//            判断是否重复抢购
            SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                    .eq("user_id", user.getId()).eq("goods_id", goodsId));
            if (seckillOrder!=null){
                model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
                return  "seckillFail";
            }
            Order order = orderService.seckill(user,goods);
            log.info("order:{}",order.toString());
            log.info("goods:{}",goods.toString());
            model.addAttribute("order",order);
            model.addAttribute("goods",goods);
            return "orderDetail";
        }

    /**
     * @author ysj
     *    秒杀页面跳转
     * @param model
     * @param user
     * @param goodsId
     * @return java.lang.String
     */
    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSecKill(Model model, User user,Long goodsId){

        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.findGoodVoByGoodsId(goodsId);
        if (goods.getStoclCount()<1) {
//            判断库存
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //         判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder!=null){
            return  RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.seckill(user,goods);
        return RespBean.success(order);
    }


    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verofyCode(User user, Long goosId,HttpServletResponse response){
        if (user==null){
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
//        设置请求头为图片类型
        response.setContentType("image/jpg");
        response.setHeader("param","No-cache");
        response.setHeader("Cache-Control","No-cache");
//       生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+
                goosId,captcha.text(),300,TimeUnit.SECONDS);

        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
          log.error("验证码生成失败",e.getMessage());
        }
    }

}

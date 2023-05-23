package com.you.seckill.controller;


import com.you.seckill.pojo.User;
import com.you.seckill.service.GoodsService;
import com.you.seckill.vo.DetailVo;
import com.you.seckill.vo.GoodsVo;
import com.you.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@RequestMapping("/goods")
@Controller
@Slf4j
public class GoodsController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    //  @Autowired
//  private UserService userService;

    @GetMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
  public String toList(Model model, User user,
                       HttpServletRequest request, HttpServletResponse response){
//        redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodVo());

//        return "goodsList";
//        如果为空，手动渲染，并且返回
        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());

        html=thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodList",html,60, TimeUnit.MINUTES);
        }
      return html;
    }
    /**
     * @author ysj
     *    跳转商品详情页
     * @param goodsId
     * @return java.lang.String
     */
 /*   @GetMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(@PathVariable Long goodsId,User user,Model model,HttpServletRequest
            request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:"+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatus=0;
        int remainSeconds=0;
//      秒杀未开始
        if (nowDate.before(startDate)){
           remainSeconds= ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        }else if (nowDate.after(endDate)){
//            秒杀已经结束
            secKillStatus=2;
        }else {
            secKillStatus=1;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("goods",goodsVo);
        WebContext context = new WebContext(request, response, request.getServletContext()
                , request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail",context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }
        return  html;
    }


*/


    @GetMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail2(@PathVariable Long goodsId, User user, Model model, HttpServletRequest
            request, HttpServletResponse response){

        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatus=0;
        int remainSeconds=0;
//      秒杀未开始
        if (nowDate.before(startDate)){
            remainSeconds= ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        }else if (nowDate.after(endDate)){
//            秒杀已经结束
            secKillStatus=2;
        }else {
            secKillStatus=1;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return  RespBean.success(detailVo);
    }
}

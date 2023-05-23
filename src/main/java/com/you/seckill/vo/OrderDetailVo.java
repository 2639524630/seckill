package com.you.seckill.vo;

import com.you.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
*
*
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {

   private Order order;

   private GoodsVo goodsVo;
}




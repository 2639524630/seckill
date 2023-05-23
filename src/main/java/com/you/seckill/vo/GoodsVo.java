package com.you.seckill.vo;


import com.you.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GoodsVo extends Goods {


    private BigDecimal seckillPrice;

    private Integer stoclCount;

    private Date startDate;

    private Date endDate;
}

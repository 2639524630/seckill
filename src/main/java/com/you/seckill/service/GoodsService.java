package com.you.seckill.service;

import com.you.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.you.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author 86152
* @description 针对表【t_goods】的数据库操作Service
* @createDate 2023-05-02 19:48:27
*/
public interface GoodsService extends IService<Goods> {

    List<GoodsVo> findGoodVo();

    GoodsVo findGoodVoByGoodsId(Long goodsId);
}

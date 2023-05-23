package com.you.seckill.mapper;

import com.you.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author 86152
* @description 针对表【t_goods】的数据库操作Mapper
* @createDate 2023-05-02 19:48:27
* @Entity com.you.seckill.pojo.Goods
*/
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * @author ysj 
     *    获取商品列表
     * @return java.util.List<com.you.seckill.vo.GoodsVo>
     */
    List<GoodsVo> findGoodVo();

    GoodsVo findGoodVoByGoodsId(Long goodsId);
}





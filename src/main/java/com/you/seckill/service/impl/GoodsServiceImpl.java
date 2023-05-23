package com.you.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.seckill.pojo.Goods;
import com.you.seckill.service.GoodsService;
import com.you.seckill.mapper.GoodsMapper;
import com.you.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86152
* @description 针对表【t_goods】的数据库操作Service实现
* @createDate 2023-05-02 19:48:27
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{
    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * @author ysj
     *
     * @return java.util.List<com.you.seckill.vo.GoodsVo>
     */
    @Override
    public List<GoodsVo> findGoodVo() {
        return goodsMapper.findGoodVo();

    }

    /**
     * @author ysj
     *    获取商品详情
     * @param goodsId
     * @return java.lang.String
     */
    @Override
    public GoodsVo findGoodVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodVoByGoodsId(goodsId);
    }
}





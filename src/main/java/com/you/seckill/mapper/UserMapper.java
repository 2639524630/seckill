package com.you.seckill.mapper;

import com.you.seckill.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
* @author 86152
* @description 针对表【t_user】的数据库操作Mapper
* @createDate 2023-05-01 20:45:33
* @Entity com.you.seckill.pojo.User
*/

public interface UserMapper extends BaseMapper<User> {

}





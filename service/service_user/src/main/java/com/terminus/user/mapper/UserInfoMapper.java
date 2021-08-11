package com.terminus.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terminus.model.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author long
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}

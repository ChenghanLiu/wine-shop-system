package com.wineshop.notice.mapper;

import com.wineshop.notice.entity.WsNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsNoticeMapper {
    List<WsNotice> selectAll();

    int insert(WsNotice notice);

    int updateById(WsNotice notice);

    int deleteById(@Param("id") Long id);
}

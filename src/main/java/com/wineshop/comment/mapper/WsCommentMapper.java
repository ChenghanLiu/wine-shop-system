package com.wineshop.comment.mapper;

import com.wineshop.comment.entity.WsComment;
import com.wineshop.comment.vo.CommentableOrderItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsCommentMapper {
    CommentableOrderItemVO selectCommentableOrderItem(@Param("userId") Long userId, @Param("orderItemId") Long orderItemId);

    int countByOrderItemId(@Param("orderItemId") Long orderItemId);

    int insert(WsComment comment);

    List<WsComment> selectVisibleByProductId(@Param("productId") Long productId);

    List<WsComment> selectByUserId(@Param("userId") Long userId);

    List<WsComment> selectAll();

    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}

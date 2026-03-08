package com.wineshop.comment.service.impl;

import com.wineshop.comment.dto.CommentCreateRequest;
import com.wineshop.comment.entity.WsComment;
import com.wineshop.comment.mapper.WsCommentMapper;
import com.wineshop.comment.service.CommentService;
import com.wineshop.comment.vo.CommentableOrderItemVO;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.ResultCode;
import com.wineshop.common.security.AuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final WsCommentMapper wsCommentMapper;

    public CommentServiceImpl(WsCommentMapper wsCommentMapper) {
        this.wsCommentMapper = wsCommentMapper;
    }

    @Override
    public void create(CommentCreateRequest request) {
        Long userId = AuthHelper.currentUser().getId();

        CommentableOrderItemVO item = wsCommentMapper.selectCommentableOrderItem(userId, request.getOrderItemId());
        if (item == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Order item is not commentable");
        }

        int existed = wsCommentMapper.countByOrderItemId(request.getOrderItemId());
        if (existed > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "This order item has already been commented");
        }

        WsComment comment = new WsComment();
        comment.setOrderId(item.getOrderId());
        comment.setOrderItemId(item.getOrderItemId());
        comment.setUserId(userId);
        comment.setProductId(item.getProductId());
        comment.setScore(request.getScore());
        comment.setContent(request.getContent());
        comment.setStatus(1);
        wsCommentMapper.insert(comment);
    }

    @Override
    public List<WsComment> listByProductId(Long productId) {
        return wsCommentMapper.selectVisibleByProductId(productId);
    }

    @Override
    public List<WsComment> listMine() {
        Long userId = AuthHelper.currentUser().getId();
        return wsCommentMapper.selectByUserId(userId);
    }
}

package com.wineshop.comment.service;

import com.wineshop.comment.dto.CommentCreateRequest;
import com.wineshop.comment.entity.WsComment;

import java.util.List;

public interface CommentService {
    void create(CommentCreateRequest request);

    List<WsComment> listByProductId(Long productId);

    List<WsComment> listMine();
}

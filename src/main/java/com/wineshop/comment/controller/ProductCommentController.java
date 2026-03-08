package com.wineshop.comment.controller;

import com.wineshop.comment.entity.WsComment;
import com.wineshop.comment.service.CommentService;
import com.wineshop.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductCommentController {

    private final CommentService commentService;

    public ProductCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}/comments")
    public Result<List<WsComment>> comments(@PathVariable("id") Long productId) {
        return Result.ok(commentService.listByProductId(productId));
    }
}

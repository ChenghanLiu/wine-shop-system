package com.wineshop.comment.controller;

import com.wineshop.comment.dto.CommentCreateRequest;
import com.wineshop.comment.entity.WsComment;
import com.wineshop.comment.service.CommentService;
import com.wineshop.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody CommentCreateRequest request) {
        commentService.create(request);
        return Result.ok();
    }

    @GetMapping("/my")
    public Result<List<WsComment>> myComments() {
        return Result.ok(commentService.listMine());
    }
}

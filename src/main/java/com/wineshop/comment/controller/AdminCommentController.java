package com.wineshop.comment.controller;

import com.wineshop.comment.entity.WsComment;
import com.wineshop.comment.mapper.WsCommentMapper;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comments")
public class AdminCommentController {

    private final WsCommentMapper wsCommentMapper;

    public AdminCommentController(WsCommentMapper wsCommentMapper) {
        this.wsCommentMapper = wsCommentMapper;
    }

    @GetMapping
    public Result<List<WsComment>> list() {
        return Result.ok(wsCommentMapper.selectAll());
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        if (wsCommentMapper.updateStatusById(id, status) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Comment not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsCommentMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Comment not found");
        }
        return Result.ok();
    }
}

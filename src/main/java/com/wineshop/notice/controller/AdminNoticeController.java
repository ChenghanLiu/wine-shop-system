package com.wineshop.notice.controller;

import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import com.wineshop.notice.entity.WsNotice;
import com.wineshop.notice.mapper.WsNoticeMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notices")
public class AdminNoticeController {

    private final WsNoticeMapper wsNoticeMapper;

    public AdminNoticeController(WsNoticeMapper wsNoticeMapper) {
        this.wsNoticeMapper = wsNoticeMapper;
    }

    @GetMapping
    public Result<List<WsNotice>> list() {
        return Result.ok(wsNoticeMapper.selectAll());
    }

    @PostMapping
    public Result<Long> create(@RequestBody WsNotice notice) {
        wsNoticeMapper.insert(notice);
        return Result.ok(notice.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WsNotice notice) {
        notice.setId(id);
        if (wsNoticeMapper.updateById(notice) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Notice not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsNoticeMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Notice not found");
        }
        return Result.ok();
    }
}

package com.wineshop.activity.controller;

import com.wineshop.activity.entity.WsActivity;
import com.wineshop.activity.mapper.WsActivityMapper;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/activities")
public class AdminActivityController {

    private final WsActivityMapper wsActivityMapper;

    public AdminActivityController(WsActivityMapper wsActivityMapper) {
        this.wsActivityMapper = wsActivityMapper;
    }

    @GetMapping
    public Result<List<WsActivity>> list() {
        return Result.ok(wsActivityMapper.selectAll());
    }

    @PostMapping
    public Result<Long> create(@RequestBody WsActivity activity) {
        wsActivityMapper.insert(activity);
        return Result.ok(activity.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WsActivity activity) {
        activity.setId(id);
        if (wsActivityMapper.updateById(activity) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Activity not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsActivityMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Activity not found");
        }
        return Result.ok();
    }
}

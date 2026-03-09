package com.wineshop.admin.controller;

import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import com.wineshop.user.entity.WsUser;
import com.wineshop.user.mapper.WsUserMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final WsUserMapper wsUserMapper;

    public AdminUserController(WsUserMapper wsUserMapper) {
        this.wsUserMapper = wsUserMapper;
    }

    @GetMapping
    public Result<List<WsUser>> list() {
        return Result.ok(wsUserMapper.selectAll());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WsUser user) {
        user.setId(id);
        if (wsUserMapper.updateByAdmin(user) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "User not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsUserMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "User not found");
        }
        return Result.ok();
    }
}

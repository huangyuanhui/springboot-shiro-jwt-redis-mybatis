package cn.gathub.controller;

import cn.gathub.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 根redis集成：在对应请求映射方法中判断redis中对应token数据是否过期
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequiresPermissions("user:list")
    @GetMapping("/list")
    public Result userList() {
        return Result.ok("获取用户信息");
    }

    @RequiresPermissions("user:add")
    @PostMapping("/add")
    public Result userAdd() {
        //
        return Result.ok("新增用户");
    }

    @RequiresPermissions("user:delete")
    @DeleteMapping("/delete")
    public Result userDelete() {
        return Result.ok("删除用户");
    }

    @RequiresPermissions("user:update")
    @PutMapping("/update")
    public Result userUpdate() {
        return Result.ok("修改用户");
    }

    @GetMapping("/test")
    public Result test() {
        return Result.ok("不用登陆直接访问的接口");
    }
}

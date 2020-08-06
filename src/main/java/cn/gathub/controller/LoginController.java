package cn.gathub.controller;

import cn.gathub.constant.CommonConstant;
import cn.gathub.entity.SysUser;
import cn.gathub.service.ISysUserService;
import cn.gathub.util.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录控制器
 * @author hyh
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class LoginController {
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录方法不走shiro自定义Realm的认证方法 而是自己实现的认证逻辑
     * @param loginUser
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/login")
    public Result<JSONObject> login(@RequestBody SysUser loginUser) throws Exception {

        String username = loginUser.getUserName();
        String password = loginUser.getPassWord();
        //根据用户名查询用户
        SysUser sysUser = sysUserService.getUserByName(username);

        //1. 校验用户是否有效
        Result<JSONObject> result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userPassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String sysPassword = sysUser.getPassWord();
        if (!sysPassword.equals(userPassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        //用户登录信息
        userInfo(sysUser, result);

        return result;
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/logout")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.ACCESS_TOKEN);
        if (CommonUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        SysUser sysUser = sysUserService.getUserByName(username);
        if (sysUser != null) {
            log.info(" 用户名:  " + sysUser.getRealName() + ",退出成功！ ");
            //清空用户Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户权限缓存：权限Perms和角色集合
            redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_ROLE + username);
            redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_PERMISSION + username);
            return Result.ok("退出登录成功！");
        } else {
            return Result.error("无效的token");
        }
    }


    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassWord();
        String username = sysUser.getUserName();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // token存入redis 并设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token, JwtUtil.EXPIRE_TIME / 1000);
        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

}

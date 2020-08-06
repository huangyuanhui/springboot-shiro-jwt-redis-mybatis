package cn.gathub.service;

import cn.gathub.entity.SysUser;
import cn.gathub.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * 用户接口
 * @author hyh
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    SysUser getUserByName(String username);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String username);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String username);

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    Result checkUserIsEffective(SysUser sysUser);

}

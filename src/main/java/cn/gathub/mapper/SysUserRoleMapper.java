package cn.gathub.mapper;

import cn.gathub.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户角色表 Mapper 接口
 * @author hyh
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 通过用户名获取角色
     * @param username
     * @return
     */
    @Select("select id from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where user_name=#{username}))")
    List<String> getRoleByUserName(@Param("username") String username);
}

package com.nwu.util;

import com.nwu.entity.User;
import com.nwu.service.impl.UserServiceImpl;
//import com.nwu.util.impl.UserDetailsImpl;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.24
 */

/**
 * 用户 spring security 的实现类
 */
//@Service("userDetailsService")
//public class MyUserDetailsService implements UserDetailsService {
public class MyUserDetailsService {

    @Resource
    private UserServiceImpl userService;

    //@Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        // 查询当前用户名的用户
//        User userByUsername = userService.findByUsername(username);
//
//        if (userByUsername == null) {
//            throw new UsernameNotFoundException("用户不存在！");
//        }
//
//        // 创建当前用户的权限
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userByUsername.getAuth());
//
//        System.out.println(authorities);
//
//        // 返回实现类的 UserDetails 对象
//        return new UserDetailsImpl(
//                userByUsername.getUsername(),
//                userByUsername.getPassword(),
//                userByUsername.getRole(),
//                userByUsername.getDepartment(),
//                userByUsername.getEnabled(),
//                authorities
//        );
//    }
}

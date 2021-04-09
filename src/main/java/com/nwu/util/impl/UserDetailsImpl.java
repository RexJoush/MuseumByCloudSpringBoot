//package com.nwu.util.impl;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
///**
// * @author Rex Joush
// * @time 2021.03.24
// */
//
///**
// * UserDetails 实现类，用来增强 User，使用 spring security 过滤方法
// */
//public class UserDetailsImpl implements UserDetails {
//
//    // 开始定义必要的属性
//    private int id;             // id
//    private String username;    // 用户名
//    private String password;    // 密码
//    private String role;        // 角色
//    private String department;  // 部门
//    private int enabled;          // 帐号是否可用
//    private Collection<? extends GrantedAuthority> authorities; // 权限
//
//
//    /*
//        构造方法
//     */
//
//    public UserDetailsImpl(int id, String username, String password, String role, String department, int enabled, Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.role = role;
//        this.department = department;
//        this.enabled = enabled;
//        this.authorities = authorities;
//    }
//
//    public UserDetailsImpl(String username, String password, String role, String department, int enabled, Collection<? extends GrantedAuthority> authorities) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//        this.department = department;
//        this.enabled = enabled;
//        this.authorities = authorities;
//    }
//
//    public UserDetailsImpl() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }
//
//    public int getEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(int enabled) {
//        this.enabled = enabled;
//    }
//
//    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return this.enabled == 1;
//    }
//
//}

package com.nwu.entity;

/**
 * @author Rex Joush
 * @time 2021.03.17
 */

import com.nwu.security.TokenDetail;

/**
 * 用户实体类
 */
public class User implements TokenDetail{

    private int id;             // id
    private String username;    // 用户名
    private String password;    // 密码
    private String role;        // 角色
    private String auth;        // 权限
    private String department;  // 部门
    private int enabled;        // 帐号是否可用


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", auth='" + auth + '\'' +
                ", department='" + department + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public User(int id, String username, String password, String role, String auth, String department, int enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.auth = auth;
        this.department = department;
        this.enabled = enabled;
    }

    public User(String username, String password, String role, String auth, String department, int enabled) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.auth = auth;
        this.department = department;
        this.enabled = enabled;
    }

    public User() {
    }
}

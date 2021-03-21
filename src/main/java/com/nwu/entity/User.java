package com.nwu.entity;

/**
 * @author Rex Joush
 * @time 2021.03.17
 */

/**
 * 用户实体类
 */
public class User {

    private int id;             // id
    private String username;    // 用户名
    private String password;    // 密码
    private String role;        // 角色
    private String department;  // 部门

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", department='" + department + '\'' +
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

    public User(String username, String password, String role, String department) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.department = department;
    }

    public User(int id, String username, String password, String role, String department) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.department = department;
    }

    public User() {
    }
}

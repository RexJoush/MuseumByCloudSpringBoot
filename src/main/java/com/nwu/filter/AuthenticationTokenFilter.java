package com.nwu.filter;

import com.nwu.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Rex Joush
 * @time 2021.03.20
 */

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * json web token 在请求头的名字
     */
    private String tokenHeader = "token";

    /**
     * 辅助操作 token 的工具类
     */
    @Resource
    private TokenUtils tokenUtils;

    /**
     * Spring Security 的核心操作服务类
     * 在当前类中将使用 UserDetailsService 来获取 userDetails 对象
     */
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 将 ServletRequest 转换为 HttpServletRequest 才能拿到请求头中的 token
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();

        // 非 login 请求，进入过滤器
        if (!requestURI.contains("login")) {

            // 尝试获取请求头的 token
            String authToken = httpRequest.getHeader(tokenHeader);
            // 尝试拿 token 中的 username
            // 若是没有 token 或者拿 username 时出现异常，那么 username 为 null
            String username = tokenUtils.getUsernameFromToken(authToken);

            // username 没有获取
            if (username == null) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print("{\"code\":\"452\",\"data\":\"\",\"message\":\"token 已过期\"}");
                return;
            }
            // System.out.println(username);


            // 如果上面解析 token 成功并且拿到了 username 并且本次会话的权限还未被写入
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // 用 UserDetailsService 从数据库中拿到用户的 UserDetails 类
//            // UserDetails 类是 Spring Security 用于保存用户权限的实体类
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//            // 检查用户带来的 token 是否有效
//            // 包括 token 和 userDetails 中用户名是否一样， token 是否过期， token 生成时间是否在最后一次密码修改时间之前
//            // 若是检查通过
////            if (this.tokenUtils.validateToken(authToken, userDetails)) {
////                // 生成通过认证
////                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
////                // 将权限写入本次会话
////                SecurityContextHolder.getContext().setAuthentication(authentication);
////            }
//
//            if (!userDetails.isEnabled()){
//                response.setCharacterEncoding("UTF-8");
//                response.setContentType("application/json;charset=UTF-8");
//                response.getWriter().print("{\"code\":\"452\",\"data\":\"\",\"message\":\"账号处于黑名单\"}");
//                return;
//            }
//        }
        }


        chain.doFilter(request, response);
    }

}

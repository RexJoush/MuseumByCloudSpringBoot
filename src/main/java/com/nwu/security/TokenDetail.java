package com.nwu.security;

/**
 * @author Rex Joush
 * @time 2021.03.20
 */

public interface TokenDetail {
    // TODO: 这里封装了一层，不直接使用 username 做参数的原因是可以方便未来增加其他要封装到 token 中的信息

    String getUsername();
}

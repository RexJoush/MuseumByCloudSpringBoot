package com.nwu.util.impl;

import com.nwu.security.TokenDetail;

/**
 * @author Rex Joush
 * @time 2021.03.20
 */

public class TokenDetailImpl implements TokenDetail {

    private final String username;

    public TokenDetailImpl(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}

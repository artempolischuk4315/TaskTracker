package com.ua.polishchuk.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Enumeration;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

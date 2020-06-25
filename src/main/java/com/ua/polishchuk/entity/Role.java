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

    public static boolean contains(String role){

        for(Role r : Role.values()){
            if(r.toString().equals(role)){
                return true;
            }
        }
        return false;
    }
}

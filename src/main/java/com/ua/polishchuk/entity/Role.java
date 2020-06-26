package com.ua.polishchuk.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

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

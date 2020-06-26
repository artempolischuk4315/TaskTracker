package com.ua.polishchuk.dto;

import com.ua.polishchuk.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements UserDetails {

    private Integer id;

    @NotBlank(message = "Empty first name")
    @Size(max = 50, message = "Too long first name")
    private String firstName;

    @NotBlank(message = "Empty last name")
    @Size(max = 50, message = "Too long last name")
    private String lastName;

    @Email(message = "Wrong email format")
    private String email;

    @NotBlank(message = "Empty password")
    private String password;

    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    @Override
    public String getUsername() { return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

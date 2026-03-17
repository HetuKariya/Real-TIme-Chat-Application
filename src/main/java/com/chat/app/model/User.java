package com.chat.app.model;

import com.chat.app.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

    private UserEntity entity;

    public User() { this.entity = new UserEntity(); }

    @JsonCreator
    public User(@JsonProperty("entity") UserEntity entity) {
        this.entity = entity;
    }

    @Override public String getUsername()   { return entity.getUsername(); }
    @Override public String getPassword()   { return entity.getPassword(); }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
    public UserEntity getEntity() { return entity; }
}
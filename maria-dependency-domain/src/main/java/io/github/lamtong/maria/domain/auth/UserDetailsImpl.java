/*
Copyright 2023 the original author, Lam Tong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.github.lamtong.maria.domain.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * {@code Spring Security} 约定程序必须实现的用户信息接口.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 7170096041635537345L;

    private Long id;

    private String username;

    private String password;

    private Integer enabled;

    private Integer accountnonexpired;

    private Integer accountnonlocked;

    private Integer credentialsnonexpired;

    private List<GrantedAuthority> authorities = new ArrayList<>();

    public UserDetailsImpl() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountnonexpired == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountnonlocked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsnonexpired == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled == 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getAccountnonexpired() {
        return accountnonexpired;
    }

    public void setAccountnonexpired(Integer accountnonexpired) {
        this.accountnonexpired = accountnonexpired;
    }

    public Integer getAccountnonlocked() {
        return accountnonlocked;
    }

    public void setAccountnonlocked(Integer accountnonlocked) {
        this.accountnonlocked = accountnonlocked;
    }

    public Integer getCredentialsnonexpired() {
        return credentialsnonexpired;
    }

    public void setCredentialsnonexpired(Integer credentialsnonexpired) {
        this.credentialsnonexpired = credentialsnonexpired;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != this.getClass()) {
            return false;
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) obj;
        return Objects.equals(this.getUsername(), userDetails.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.username);
    }

    @Override
    public String toString() {
        return "UserDetailsImpl{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", accountnonexpired=" + accountnonexpired +
                ", accountnonlocked=" + accountnonlocked +
                ", credentialsnonexpired=" + credentialsnonexpired +
                ", authorities=" + authorities +
                '}';
    }

}

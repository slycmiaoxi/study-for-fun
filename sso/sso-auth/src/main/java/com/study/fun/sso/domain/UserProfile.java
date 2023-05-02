package com.study.fun.sso.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String userId;
    private String userType;
    private String username;
    private String password;
    private Integer userStatus;
}

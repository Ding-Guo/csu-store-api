package org.csu.api.service;

import org.csu.api.common.CommonResponse;
import org.csu.api.domain.User;

public interface UserService {

    //用户登录
    CommonResponse<User> login(String username, String password);
}

package org.csu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.api.common.CommonResponse;
import org.csu.api.domain.User;
import org.csu.api.persistence.UserMapper;
import org.csu.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public CommonResponse<User> login(String username, String password){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username" , username);
        queryWrapper.eq("password" , password);
        //todo:密码的加密解密处理
        User user = userMapper.selectOne(queryWrapper);

        if(user == null){
            return CommonResponse.createForError("用户名或密码错误");
        }

        user.setPassword(null);
        return CommonResponse.createForSuccess("登录成功" , user);
    }


}

package org.csu.api.controller.front;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.csu.api.common.CommonResponse;
import org.csu.api.domain.User;
import org.csu.api.dto.LoginUserDTO;
import org.csu.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //DTO:客户端提交数据时数据的封装对象
    //VO：View Object，服务端向客户端返回数据时的封装对象
    //VO：Value Object，业务逻辑层和DAO层交换的封装对象
    //BO：业务对象，领域对象DO
    @PostMapping("/user/login")
    public CommonResponse<User> login(@Valid @RequestBody LoginUserDTO userDTO,
                                      HttpSession session){
        CommonResponse<User> result = userService.login(userDTO.getUsername(), userDTO.getPassword());
        if(result.isSuccess()){
            session.setAttribute("loginUser" , result.getData());
        }
        return result;
    }

}

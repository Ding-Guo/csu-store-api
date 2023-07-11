package org.csu.api.controller.front;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.*;
import org.csu.api.service.UserService;
import org.csu.api.util.RedisCache;
import org.csu.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*",allowCredentials = "true")
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    //DTO:客户端提交数据时数据的封装对象，以及各个层之间传递数据
    //VO：View Object，服务端向客户端返回数据时的封装对象
    //VO：Value Object，业务逻辑层和DAO层交换的封装对象
    //BO：业务对象，领域对象DO
    @Resource
    private RedisCache redisCache;
    @PostMapping("login")
    public CommonResponse<UserVO> login(@Valid @RequestBody LoginUserDTO loginUserDTO
                                        ) {
        CommonResponse<UserVO> result = userService.login(loginUserDTO);
//        if (result.isSuccess()) {
//
//            session.setAttribute(CONSTANT.LOGIN_USER, result.getData());
//        }
        return result;
    }

    @PostMapping("check_field")
    public CommonResponse<Object> checkField(@Valid @RequestBody CheckUserFieldDTO checkUserFieldDTO) {
        return userService.checkField(checkUserFieldDTO.getFieldName(), checkUserFieldDTO.getFieldValue(),0);
    }

    @PostMapping("register")
    public CommonResponse<Object> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        if(!StringUtils.equals(registerUserDTO.getPassword(),registerUserDTO.getConfirmPassword())){
            return CommonResponse.createForError("两次密码不一致");
        }
        return userService.register(registerUserDTO);
    }

    @GetMapping("get_forget_question")
    public CommonResponse<String> getForgetQuestion(
            @RequestParam @NotBlank(message = "用户名不能为空") String username) {
        return userService.getForgetQuestion(username);
    }

    @PostMapping("check_forget_answer")
    public CommonResponse<String> checkForgetAnswer(
            @Valid @RequestBody CheckAnswerUserDTO checkAnswerUserDTO){
        return userService.checkForgetAnswer(
                checkAnswerUserDTO.getUsername(), checkAnswerUserDTO.getQuestion(), checkAnswerUserDTO.getAnswer());
    }

    @PostMapping("reset_forget_password")
    public CommonResponse<String> resetForgetPassword(
            @Valid @RequestBody ResetUserDTO resetUserDTO){
        return userService.resetForgetPassword(
                resetUserDTO.getUsername(), resetUserDTO.getNewPassword(), resetUserDTO.getForgetToken());
    }

    @PostMapping("sign_out")
    public CommonResponse<Object> signOut(HttpServletRequest request) {
//        session.removeAttribute(CONSTANT.LOGIN_USER);
        String token = request.getHeader("Authorization");
        redisCache.deleteObject(token);
        return CommonResponse.createForSuccess("退出登录成功");
    }
    @PostMapping("get_user_info")
    public CommonResponse<UserVO> getUserDetail(HttpServletRequest request) {
//        UserVO userVO = (UserVO) httpSession.getAttribute(CONSTANT.LOGIN_USER);
//        System.out.println(userVO);
//
//        return userVO == null ? CommonResponse.createForError(ResponseCode.ERROR.getCode(), "用户未登录") :
//                CommonResponse.createForSuccess(userVO);

        String token = request.getHeader("Authorization");
        System.out.println(userService.getUserVOByToken(token));
        return CommonResponse.createForSuccess(userService.getUserVOByToken(token));
    }
    @PostMapping("reset_password")
    public CommonResponse<String> reset_password(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO,
                                                 HttpServletRequest request) {
//        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
//        if (loginUser == null)
//            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
//                    ResponseCode.NEED_LOGIN.getDescription());
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        return userVO == null ? CommonResponse.createForError("密码重设失败") :
                userService.resetPassword(userVO.getUsername(), resetPasswordDTO);
    }


    @PostMapping("update_user_info")
    public CommonResponse<String> update_user_info(@Valid @RequestBody UpdateUserDTO updateUserDTO,
                                                   HttpServletRequest request) {
//        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
//        if (loginUser == null)
//            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
//                    ResponseCode.NEED_LOGIN.getDescription());
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        CommonResponse<Object> result = userService.updateUser(userVO.getUsername(), updateUserDTO);
        if (result.isSuccess()) {

            return CommonResponse.createForSuccessMessage("SUCCESS");
        }
        return CommonResponse.createForError(result.getMessage() + ",修改个人信息失败");
    }
}

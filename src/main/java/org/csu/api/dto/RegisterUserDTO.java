package org.csu.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "电话号码不能为空")
    private String phone;
    private String question;
    private String answer;
}

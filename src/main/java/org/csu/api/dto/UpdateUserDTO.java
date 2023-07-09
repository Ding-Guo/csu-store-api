package org.csu.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserDTO {

    @NotBlank(message = "用户名不为空")
    private String username;


    @NotBlank(message = "邮箱不为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "电话号码不为空")
    private String phone;

    private String question;

    private String answer;
}

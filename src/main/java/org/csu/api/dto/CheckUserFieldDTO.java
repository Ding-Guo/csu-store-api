package org.csu.api.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckUserFieldDTO {
    @NotBlank(message = "字段名不能为空")
    private String fieldName;
    @NotBlank(message = "字段值不能为空")
    private String fieldValue;
}

package org.csu.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressIdDTO {
    @NotNull(message = "地址ID不能为空")
    private Integer AddressId;
}

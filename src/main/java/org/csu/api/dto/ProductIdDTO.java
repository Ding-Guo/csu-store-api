package org.csu.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductIdDTO {
    @NotNull(message = "商品ID不能为空")
    private Integer productId;
}

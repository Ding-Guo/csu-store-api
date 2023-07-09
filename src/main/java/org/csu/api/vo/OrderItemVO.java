package org.csu.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class OrderItemVO {
    private Integer id;


    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentPrice;

    private Integer quantity;

    private BigDecimal totalPrice;
}

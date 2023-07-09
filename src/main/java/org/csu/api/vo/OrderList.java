package org.csu.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class OrderList {
    private List<OrderItemVO> orderItemVoList;

    private BigDecimal paymentPrice;

    private String imageServer;
}

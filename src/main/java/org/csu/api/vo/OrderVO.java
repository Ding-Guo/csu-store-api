package org.csu.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class OrderVO {
    private Integer id;

    private Long orderNo;

    private Integer userId;

    private BigDecimal paymentPrice;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private LocalDateTime paymentTime;

    private LocalDateTime sendTime;

    private LocalDateTime endTime;

    private LocalDateTime closeTime;

    private AddressVO addressVO;

    private List<OrderItemVO> orderItemVoList;

    private String imageServer;
}

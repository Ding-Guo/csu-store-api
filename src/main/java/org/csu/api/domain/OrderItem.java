package org.csu.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mystore_order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "order_no")
    private Long orderNo;
    @TableField(value = "product_id")
    private Integer productId;
    @TableField(value = "product_name")
    private String productName;
    @TableField(value = "product_image")
    private String productImage;
    @TableField(value = "current_price")

    private BigDecimal currentPrice;

    private Integer quantity;
    @TableField(value = "total_price")

    private BigDecimal totalPrice;

    @TableField(value = "create_time")
    private LocalDateTime createTime;


    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}

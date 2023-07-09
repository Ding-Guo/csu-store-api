package org.csu.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAddressDTO {

    @NotBlank(message = "id不能为空")
    private Integer id;

    @NotBlank(message = "收货人姓名不能为空")
    private String addressName;

    @NotBlank(message = "收货人固定电话不能为空")
    private String addressPhone;

    @NotBlank(message = "收货人手机号码不能为空")
    private String addressMobile;

    @NotBlank(message = "收货人省份不能为空")
    private String addressProvince;

    @NotBlank(message = "收货人城市不能为空")
    private String addressCity;

    @NotBlank(message = "收货人区/县不能为空")
    private String addressDistrict;

    @NotBlank(message = "收货人详细地址不能为空")
    private String addressDetail;

    @NotBlank(message = "收货人邮编不能为空")
    private String addressZip;

}

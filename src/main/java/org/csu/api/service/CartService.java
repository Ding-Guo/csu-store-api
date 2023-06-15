package org.csu.api.service;

import org.csu.api.common.CommonResponse;

public interface CartService {
    //添加一条购物车项
    CommonResponse<String> addCart(Integer userId,Integer productId,Integer quantity);
}

package org.csu.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.csu.api.common.CommonResponse;
import org.csu.api.persistence.CartItemMapper;
import org.csu.api.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("categoryService")
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public CommonResponse<String> addCart(Integer userId, Integer productId, Integer quantity) {
        return null;
    }
}

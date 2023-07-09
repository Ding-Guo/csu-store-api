package org.csu.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.api.common.CommonResponse;
import org.csu.api.vo.CartItemListVO;
import org.csu.api.vo.CartVO;

import java.util.List;

public interface CartService {
    //添加一条购物车项
    CommonResponse<String> addCart(Integer userId,Integer productId,Integer quantity);
    CommonResponse<String> updateCart(Integer userId,Integer productId,Integer quantity);
    CommonResponse<String> deleteCart(Integer userId, List<Integer> productIds);
    CommonResponse<CartVO> getCartList(Integer userId);
    CommonResponse<CartVO> setAllChecked(Integer userId);
    CommonResponse<CartVO> setAllUnchecked(Integer userId);

    CommonResponse<CartVO> setCartItemChecked(Integer userId,Integer productId);

    CommonResponse<CartVO> setCartItemUnchecked(Integer userId,Integer productId);
    CommonResponse<String> getCartCount(Integer userId);


}

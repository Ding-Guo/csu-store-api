package org.csu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.domain.CartItem;
import org.csu.api.domain.Product;
import org.csu.api.persistence.CartItemMapper;
import org.csu.api.persistence.ProductMapper;
import org.csu.api.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.time.LocalDateTime;

@Service("categoryService")
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ProductMapper productMapper;
    @Override
    public CommonResponse<String> addCart(Integer userId, Integer productId, Integer quantity) {
        //判断商品ID是否存在
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",productId).eq("status", CONSTANT.ProductStatus.ON_SALE.getCode());
        Product product = productMapper.selectOne((queryWrapper));
        if (product == null){
            return CommonResponse.createForError("商品ID不存在或者已经下架");
        }
        int productStock = product.getStock();
        //查询
        QueryWrapper<CartItem> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("user_id",userId).eq("product_id",productId);
        CartItem cartItem = cartItemMapper.selectOne((queryWrapper1));

        int result = 0;
        if (cartItem == null){
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            if (productStock<quantity){
                quantity = productStock;
            }
            cartItem.setQuantity(quantity);
            cartItem.setChecked(CONSTANT.CART_ITEM_STATUS.CHECKED);
            cartItem.setCreatTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            result = cartItemMapper.insert(cartItem);
        }else {
            if (productStock < quantity + cartItem.getQuantity()){
                quantity = productStock;
            }
            UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", cartItem.getId());
            updateWrapper.set("update_time",LocalDateTime.now());
            updateWrapper.set("quantity",quantity);
            result = cartItemMapper.update(cartItem,updateWrapper);
        }
        if (result ==1){
            return CommonResponse.createForSuccessMessage("成功");
        }
        return CommonResponse.createForError("添加失败");
    }
}

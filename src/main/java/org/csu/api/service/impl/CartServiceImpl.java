package org.csu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.domain.CartItem;
import org.csu.api.domain.Product;
import org.csu.api.persistence.CartItemMapper;
import org.csu.api.persistence.ProductMapper;
import org.csu.api.service.CartService;
import org.csu.api.util.ImageServerConfig;
import org.csu.api.vo.CartItemListVO;
import org.csu.api.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("cateService")
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartItemMapper cartItemMapper;
    @Resource
    private ImageServerConfig imageServerConfig;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public CommonResponse<CartVO> addCart(Integer userId, Integer productId, Integer quantity) {
        //判断商品ID是否存在
        List<Integer> productLists = new ArrayList<>();
        productLists.add(productId);
        if (!this.checkInCart(productLists)){
            return CommonResponse.createForError("商品ID不存在或者已经下架");
        }
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",productId).eq("status", CONSTANT.ProductStatus.ON_SALE.getCode());
        Product product = productMapper.selectOne((queryWrapper));
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
            cartItem.setCreateTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            result = cartItemMapper.insert(cartItem);
        }else {
            if (productStock < quantity + cartItem.getQuantity()){
                quantity = productStock;
            }
            result = this.updateCart(cartItem,quantity);
        }
        if (result ==1){
            return this.getCartList(userId);
        }
        return CommonResponse.createForError("添加失败");
    }

    @Override
    public CommonResponse<CartVO> updateCart(Integer userId, Integer productId, Integer quantity) {
        List<Integer> productLists = new ArrayList<>();
        productLists.add(productId);
        if (!this.checkInCart(productLists)){
            return CommonResponse.createForError("商品ID不存在或者已经下架");
        }
        //查询
        QueryWrapper<CartItem> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("user_id",userId).eq("product_id",productId);
        CartItem cartItem = cartItemMapper.selectOne((queryWrapper1));

        int result = 0;
        if (cartItem == null){
            return CommonResponse.createForError("商品不在购物车");
        }else {
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",productId).eq("status", CONSTANT.ProductStatus.ON_SALE.getCode());
            Product product = productMapper.selectOne((queryWrapper));
            int productStock = product.getStock();
            if (productStock < quantity + cartItem.getQuantity()){
                quantity = productStock;
            }
            result = this.updateCart(cartItem,quantity);
        }
        if (result ==1){
            return this.getCartList(userId);
        }
        return CommonResponse.createForError("添加失败");
    }

    @Override
    public CommonResponse<CartVO> deleteCart(Integer userId, List<Integer> productIds) {
        if (!this.checkInCart(productIds)){
            return CommonResponse.createForError("商品ID不存在或者已经下架");
        }
        for (int i=0;i<productIds.size();i++){
            System.out.println(userId);
            System.out.println(productIds.get(i));
            QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",userId).eq("product_id",productIds.get(i));
            cartItemMapper.delete(queryWrapper);
        }

        return this.getCartList(userId);
    }

    @Override
    public CommonResponse<CartVO> getCartList(Integer userId) {
        CartVO cartVO = new CartVO();
        QueryWrapper<CartItem> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", userId);
        List<CartItem> cartList = cartItemMapper.selectList(cartQueryWrapper);
        List<CartItemListVO> cartItemVOList = new ArrayList<>();
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        boolean allSelected = true;

        if (CollectionUtils.isNotEmpty(cartList)) {
            for (CartItem cart : cartList) {
                CartItemListVO cartItemVO = new CartItemListVO();
                cartItemVO.setId(cart.getId());
                cartItemVO.setUserId(cart.getUserId());
                cartItemVO.setProductId(cart.getProductId());
                cartItemVO.setChecked(cart.getChecked());
                Product product = productMapper.selectById(cart.getProductId());
                if (product != null) {
                    cartItemVO.setProductName(product.getName());
                    cartItemVO.setProductSubtitle(product.getSubtitle());
                    cartItemVO.setProductMainImage(product.getMainImage());

                    cartItemVO.setProductPrice(product.getPrice());
                    cartItemVO.setProductStock(product.getStock());

                    if (product.getStock() >= cart.getQuantity()) {
                        cartItemVO.setQuantity(cart.getQuantity());
                        cartItemVO.setCheckStock(true);
                    } else {
                        cartItemVO.setQuantity(product.getStock());
                        CartItem updateStockCart = new CartItem();
                        UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id", cart.getId()).set("quantity", product.getStock());
                        cartItemMapper.update(updateStockCart, updateWrapper);
                        cartItemVO.setCheckStock(false);
                    }
                    BigDecimal num = new BigDecimal(cartItemVO.getQuantity());
                    cartItemVO.setProductTotalPrice(cartItemVO.getProductPrice().multiply(num));
                    if (cart.getChecked() == CONSTANT.CART_STATUS.CHECKED) {
                        cartTotalPrice = cartTotalPrice.add(cartItemVO.getProductTotalPrice());
                    } else {
                        allSelected = false;
                    }
                    cartItemVOList.add(cartItemVO);
                }
            }
        }
        cartVO.setCartItemVOList(cartItemVOList);
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setAllSelected(allSelected);
        cartVO.setProductImageServer(imageServerConfig.getUrl());
        return CommonResponse.createForSuccess(cartVO);
    }

    @Override
    public CommonResponse<CartVO> setAllChecked(Integer userId) {
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<CartItem> cartItemList= cartItemMapper.selectList(queryWrapper);
        for (int i=0;i<cartItemList.size();i++){
            CartItem cartItem = cartItemList.get(i);
            UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", userId);
            updateWrapper.eq("id", cartItem.getId());
            updateWrapper.set("update_time",LocalDateTime.now());
            updateWrapper.set("checked",CONSTANT.CART_ITEM_STATUS.CHECKED);
            cartItemMapper.update(cartItem,updateWrapper);
        }
        return this.getCartList(userId);
    }
    @Override
    public CommonResponse<CartVO> setAllUnchecked(Integer userId) {
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<CartItem> cartItemList= cartItemMapper.selectList(queryWrapper);
        for (int i=0;i<cartItemList.size();i++){
            CartItem cartItem = cartItemList.get(i);
            UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", userId);
            updateWrapper.set("update_time",LocalDateTime.now());
            updateWrapper.set("checked",CONSTANT.CART_ITEM_STATUS.UNCHECKED);
            cartItemMapper.update(cartItem,updateWrapper);
        }
        return this.getCartList(userId);
    }

    @Override
    public CommonResponse<CartVO> setCartItemChecked(Integer userId,Integer productId) {
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("product_id",productId);
        CartItem cartItem = cartItemMapper.selectOne((queryWrapper));

        UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", cartItem.getId());
        updateWrapper.set("update_time",LocalDateTime.now());
        updateWrapper.set("checked",CONSTANT.CART_ITEM_STATUS.CHECKED);
        cartItemMapper.update(cartItem,updateWrapper);
        return this.getCartList(userId);
    }

    @Override
    public CommonResponse<CartVO> setCartItemUnchecked(Integer userId,Integer productId) {
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("product_id",productId);
        CartItem cartItem = cartItemMapper.selectOne((queryWrapper));

        UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", cartItem.getId());
        updateWrapper.set("update_time",LocalDateTime.now());
        updateWrapper.set("checked",CONSTANT.CART_ITEM_STATUS.UNCHECKED);
        cartItemMapper.update(cartItem,updateWrapper);
        return this.getCartList(userId);
    }

    @Override
    public CommonResponse<String> getCartCount(Integer userId) {
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<CartItem> cartItemList= cartItemMapper.selectList(queryWrapper);
//        return cartItemMapper.selectList(queryWrapper);
        Integer num = 0;
        for (int i=0;i<cartItemList.size();i++){
            num += cartItemList.get(i).getQuantity();
        }
        return CommonResponse.createForSuccess(num.toString());
    }

    private Boolean checkInCart(List<Integer> productLists){
        for (int i=0;i<productLists.size();i++){
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", productLists.get(i)).eq("status", CONSTANT.ProductStatus.ON_SALE.getCode());
            Product product = productMapper.selectOne((queryWrapper));
            if (product == null) {
                return false;
            }
        }
        return true;
    }

    private int updateCart(CartItem cartItem,Integer quantity){
        UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", cartItem.getId());
        updateWrapper.set("update_time",LocalDateTime.now());
        updateWrapper.set("quantity",quantity);
        return cartItemMapper.update(cartItem,updateWrapper);
    }
}

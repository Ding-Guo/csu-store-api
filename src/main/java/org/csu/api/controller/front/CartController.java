package org.csu.api.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.PostCartDTO;
import org.csu.api.dto.ProductIdDTO;
import org.csu.api.service.CartService;
import org.csu.api.vo.CartItemListVO;
import org.csu.api.vo.CartVO;
import org.csu.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart/")
@CrossOrigin(originPatterns = "*",allowCredentials = "true")
@Validated
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("add")
    public CommonResponse<String> addCart(@RequestBody PostCartDTO cartDTO, HttpSession session){

        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }

        return cartService.addCart(loginUser.getId(),cartDTO.getProductId(),cartDTO.getQuantity());
    }
    @PostMapping("update")
    public CommonResponse<String> updateCart(@RequestBody PostCartDTO cartDTO, HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.updateCart(loginUser.getId(),cartDTO.getProductId(),cartDTO.getQuantity());
    }

    @PostMapping("delete")
    public CommonResponse<String> deleteCart(@RequestBody ProductIdDTO productIdDTO, HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        System.out.println(productIdDTO);
        List<Integer> productIds =new ArrayList<>();
        productIds.add(productIdDTO.getProductId());

        return cartService.deleteCart(loginUser.getId(),productIds);
    }

    @PostMapping("list")
    public CommonResponse<CartVO> getCartList(HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.getCartList(loginUser.getId());
    }

    @PostMapping("set_all_checked")
    public CommonResponse<CartVO> setAllChecked(HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setAllChecked(loginUser.getId());
    }

    @PostMapping("set_all_unchecked")
    public CommonResponse<CartVO> setAllUnchecked(HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setAllUnchecked(loginUser.getId());
    }
    @PostMapping("set_cart_item_checked")
    public CommonResponse<CartVO> setCartItemChecked(@RequestBody ProductIdDTO productIdDTO, HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setCartItemChecked(loginUser.getId(),productIdDTO .getProductId());
    }
    @PostMapping("set_cart_item_unchecked")
    public CommonResponse<CartVO> setCartItemUnchecked(@RequestBody ProductIdDTO productIdDTO,HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setCartItemUnchecked(loginUser.getId(),productIdDTO .getProductId());
    }
    @PostMapping("get_cart_count")
    public CommonResponse<String> getCartCount(HttpSession session){
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.getCartCount(loginUser.getId());
    }
}

package org.csu.api.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.PostCartDTO;
import org.csu.api.dto.ProductIdDTO;
import org.csu.api.service.CartService;
import org.csu.api.service.UserService;
import org.csu.api.vo.CartItemListVO;
import org.csu.api.vo.CartVO;
import org.csu.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
    @Autowired
    private UserService userService;
    @PostMapping("add")
    public CommonResponse<CartVO> addCart(@RequestBody PostCartDTO cartDTO, HttpServletRequest request){

        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.addCart(userVO.getId(),cartDTO.getProductId(),cartDTO.getQuantity());
    }
    @PostMapping("update")
    public CommonResponse<CartVO> updateCart(@RequestBody PostCartDTO cartDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.updateCart(userVO.getId(),cartDTO.getProductId(),cartDTO.getQuantity());
    }

    @PostMapping("delete")
    public CommonResponse<CartVO> deleteCart(@RequestBody ProductIdDTO productIdDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        System.out.println(productIdDTO);
        List<Integer> productIds =new ArrayList<>();
        productIds.add(productIdDTO.getProductId());

        return cartService.deleteCart(userVO.getId(),productIds);
    }

    @PostMapping("list")
    public CommonResponse<CartVO> getCartList(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.getCartList(userVO.getId());
    }

    @PostMapping("set_all_checked")
    public CommonResponse<CartVO> setAllChecked(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setAllChecked(userVO.getId());
    }

    @PostMapping("set_all_unchecked")
    public CommonResponse<CartVO> setAllUnchecked(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setAllUnchecked(userVO.getId());
    }
    @PostMapping("set_cart_item_checked")
    public CommonResponse<CartVO> setCartItemChecked(@RequestBody ProductIdDTO productIdDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setCartItemChecked(userVO.getId(),productIdDTO .getProductId());
    }
    @PostMapping("set_cart_item_unchecked")
    public CommonResponse<CartVO> setCartItemUnchecked(@RequestBody ProductIdDTO productIdDTO,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.setCartItemUnchecked(userVO.getId(),productIdDTO .getProductId());
    }
    @PostMapping("get_cart_count")
    public CommonResponse<String> getCartCount(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.getCartCount(userVO.getId());
    }
}

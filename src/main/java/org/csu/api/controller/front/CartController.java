package org.csu.api.controller.front;

import jakarta.servlet.http.HttpSession;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.PostCartDTO;
import org.csu.api.service.CartService;
import org.csu.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/")
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
}

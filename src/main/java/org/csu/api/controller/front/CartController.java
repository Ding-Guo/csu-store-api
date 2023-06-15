package org.csu.api.controller.front;

import jakarta.servlet.http.HttpSession;
import org.csu.api.common.CommonResponse;
import org.csu.api.dto.PostCartDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/")
@Validated
public class CartController {
    @PostMapping("add")
    public CommonResponse<Object> addCart(@RequestBody PostCartDTO cartDTO, HttpSession session){
        return null;
    }
}

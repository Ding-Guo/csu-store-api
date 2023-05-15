package org.csu.api.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.csu.api.common.CommonResponse;

@RestController
public class ProductController {
    @GetMapping("detail")
    public CommonResponse<Object> getProductDetail(
            @RequestParam @NotNull(message = "商品ID不能为空") Integer productId){

        return null;
    }

    @GetMapping("list")
    public CommonResponse<Page<Object>> getProductList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "") String orderBy,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "2") int pageSize){

        return null;
    }
}

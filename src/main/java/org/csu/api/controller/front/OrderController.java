package org.csu.api.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.servlet.http.HttpSession;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.AddressIdDTO;
import org.csu.api.service.OrderService;
import org.csu.api.vo.OrderList;
import org.csu.api.vo.OrderVO;
import org.csu.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*",allowCredentials = "true")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public CommonResponse<OrderVO> createOrder(@RequestBody AddressIdDTO addressIdDTO, HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDescription());
        return orderService.createOrder(addressIdDTO.getAddressId(), loginUser.getId());
    }

    @GetMapping("/cart_item_list")
    public CommonResponse<OrderList> getOrderList(HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDescription());
        CommonResponse<OrderList> orderList = orderService.getOrderList(loginUser.getId());
        System.out.println(orderList.getData().getOrderItemVoList());
        return  orderList;
    }

    @GetMapping("/detail")
    public CommonResponse<OrderVO> getOrderDetail(@RequestParam String orderNo,HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDescription());
        return orderService.getOrderDetail(orderNo);
    }
    @GetMapping("/list")
    public CommonResponse<Page<OrderVO>> getOrderList(@RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "5") int pageSize,
                                                      HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDescription());
        return orderService.getOrderList(pageNum,pageSize,loginUser.getId());
    }

    @GetMapping("/cancel")
    public CommonResponse<Object> cancelOrder(@RequestParam String orderNo,HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDescription());
        return orderService.cancelOrder(orderNo);
    }


}

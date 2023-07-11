package org.csu.api.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.AddressIdDTO;
import org.csu.api.service.OrderService;
import org.csu.api.service.UserService;
import org.csu.api.util.RedisCache;
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
    @Autowired
    private UserService userService;
    @Resource
    private RedisCache redisCache;
    @PostMapping("/create")
    public CommonResponse<OrderVO> createOrder(@RequestBody AddressIdDTO addressIdDTO, HttpServletRequest request) {
//        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
//        if (loginUser == null)
//            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
//                    ResponseCode.NEED_LOGIN.getDescription());
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        return orderService.createOrder(addressIdDTO.getAddressId(), userVO.getId());
    }

    @GetMapping("/cart_item_list")
    public CommonResponse<OrderList> getOrderList(HttpServletRequest request) {
//        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
//        if (loginUser == null)
//            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
//                    ResponseCode.NEED_LOGIN.getDescription());
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        CommonResponse<OrderList> orderList = orderService.getOrderList(userVO.getId());
        System.out.println(orderList.getData().getOrderItemVoList());
        return  orderList;
    }

    @GetMapping("/detail")
    public CommonResponse<OrderVO> getOrderDetail(@RequestParam String orderNo,HttpServletRequest request) {
//        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
//        if (loginUser == null)
//            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),
//                    ResponseCode.NEED_LOGIN.getDescription());
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.getOrderDetail(orderNo);
    }
    @GetMapping("/list")
    public CommonResponse<Page<OrderVO>> getOrderList(@RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "5") int pageSize,
                                                      HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.getOrderList(pageNum,pageSize,userVO.getId());
    }

    @GetMapping("/cancel")
    public CommonResponse<Object> cancelOrder(@RequestParam String orderNo,HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.cancelOrder(orderNo);
    }


}

package org.csu.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.api.common.CommonResponse;
import org.csu.api.vo.OrderList;
import org.csu.api.vo.OrderVO;

public interface OrderService {
    CommonResponse<OrderList> getOrderList(Integer userId);

    CommonResponse<OrderVO> createOrder(Integer addressId, Integer userId);

    CommonResponse<OrderVO> getOrderDetail(String orderNo);

    CommonResponse<Page<OrderVO>> getOrderList(int pageNum, int pageSize, Integer userId);

    CommonResponse<Object> cancelOrder(String orderNo);
}

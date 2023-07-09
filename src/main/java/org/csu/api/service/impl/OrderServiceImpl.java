package org.csu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.domain.CartItem;
import org.csu.api.domain.Order;
import org.csu.api.domain.OrderItem;
import org.csu.api.domain.Product;
import org.csu.api.persistence.CartItemMapper;
import org.csu.api.persistence.OrderItemMapper;
import org.csu.api.persistence.OrderMapper;
import org.csu.api.persistence.ProductMapper;
import org.csu.api.service.AddressService;
import org.csu.api.service.OrderService;

import org.csu.api.util.ImageServerConfig;
import org.csu.api.util.ListBeanUtils;
import org.csu.api.vo.OrderItemVO;
import org.csu.api.vo.OrderList;
import org.csu.api.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service("orderService")
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartItemMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AddressService addressService;

    @Resource
    private ImageServerConfig imageServerConfig;


    @Override
    public CommonResponse<OrderList> getOrderList(Integer userId) {
        OrderList orderList = getCartCheckedList(userId);
        if (CollectionUtils.isEmpty(orderList.getOrderItemVoList())){
            System.out.println(1);
            return CommonResponse.createForError("购物车为空");
        }
        System.out.println(orderList);
        return CommonResponse.createForSuccess(orderList);
    }

    private OrderList getCartCheckedList(Integer userId) {
        OrderList orderList = new OrderList();
        QueryWrapper<CartItem> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", userId);
        List<CartItem> cartItemList = cartMapper.selectList(cartQueryWrapper);
        List<OrderItemVO> orderItemVoList = Lists.newArrayList();
        BigDecimal paymentPrice = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(cartItemList)) {
            for (CartItem cartItem : cartItemList) {
                if (cartItem.getChecked() == CONSTANT.CART_ITEM_STATUS.CHECKED) {
                    OrderItemVO orderItemVO = new OrderItemVO();
                    orderItemVO.setId(cartItem.getId());
                    orderItemVO.setProductId(cartItem.getProductId());
                    //orderItemVo.setQuantity(cart.getQuantity());
                    Product product = productMapper.selectById(cartItem.getProductId());
                    if (product != null) {
                        orderItemVO.setProductName(product.getName());
                        orderItemVO.setProductImage(product.getMainImage());
                        orderItemVO.setCurrentPrice(product.getPrice());
                        if (product.getStock() >= cartItem.getQuantity()) {
                            orderItemVO.setQuantity(cartItem.getQuantity());
                        } else {
                            orderItemVO.setQuantity(product.getStock());
                            CartItem updateStockCart = new CartItem();
                            UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
                            updateWrapper.eq("id", cartItem.getId()).set("quantity", product.getStock());
                            cartMapper.update(updateStockCart, updateWrapper);
                        }
                        BigDecimal num = new BigDecimal(orderItemVO.getQuantity());
//                        cartItemListVO.setProductTotalPrice(product.getPrice().multiply(num));
//                        orderItemVO.setTotalPrice(BigDecimalUtil.multiply(orderItemVO.getCurrentPrice().doubleValue(), orderItemVO.getQuantity()));
                        orderItemVO.setTotalPrice(orderItemVO.getCurrentPrice().multiply(num));
//                        paymentPrice = BigDecimalUtil.add(paymentPrice.doubleValue(),
//                                orderItemVO.getTotalPrice().doubleValue());
                        paymentPrice = orderItemVO.getTotalPrice();
                        orderItemVoList.add(orderItemVO);
                    }

                }
            }
        }
        orderList.setPaymentPrice(paymentPrice);
        orderList.setOrderItemVoList(orderItemVoList);
        orderList.setImageServer(imageServerConfig.getUrl());
        return orderList;

    }

    @Override
    public CommonResponse<OrderVO> createOrder(Integer addressId, Integer userId) {
        //获取购物车中选中的商品
        OrderList orderList = getCartCheckedList(userId);
        if (CollectionUtils.isEmpty(orderList.getOrderItemVoList()))
            return CommonResponse.createForError("购物车为空,创建订单失败");
        //生成订单
        Order order = new Order();
        order.setUserId(userId);
        Long orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setAddressId(addressId);
        order.setPaymentPrice(orderList.getPaymentPrice());
        order.setPaymentType(CONSTANT.OrderPayType.AliPay.getCode());
        order.setStatus(CONSTANT.OrderStatus.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentTime(null);
        order.setSendTime(null);
        order.setEndTime(null);
        order.setCloseTime(null);
        if (orderMapper.insert(order) > 0) {
            for (OrderItemVO orderItemVo : orderList.getOrderItemVoList()) {
                OrderItem orderItem = new OrderItem();
                BeanUtils.copyProperties(orderItemVo, orderItem);
                orderItem.setOrderNo(orderNo);
                orderItem.setUserId(userId);
                if (orderItemMapper.insert(orderItem) <= 0) {
                    CommonResponse.createForError("订单项创建失败");
                }
            }
        } else
            return CommonResponse.createForError("订单创建失败");

        //返回VO
        OrderVO orderVo = new OrderVO();
        BeanUtils.copyProperties(order, orderVo);
        orderVo.setAddressVO(addressService.findAddress(addressId).getData());
        orderVo.setOrderItemVoList(orderList.getOrderItemVoList());
        orderVo.setImageServer(imageServerConfig.getUrl());
        return CommonResponse.createForSuccess(orderVo);


    }

    @Override
    public CommonResponse<OrderVO> getOrderDetail(String orderNo) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(orderQueryWrapper);
        if (order == null)
            return CommonResponse.createForError("订单不存在");
        OrderVO orderVo = new OrderVO();
        BeanUtils.copyProperties(order, orderVo);
        orderVo.setAddressVO(addressService.findAddress(order.getAddressId()).getData());
        orderVo.setImageServer(imageServerConfig.getUrl());
        QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
        orderItemQueryWrapper.eq("order_no", orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);
        List<OrderItemVO> orderItemVoList = ListBeanUtils.copyListProperties(orderItemList, OrderItemVO::new);
        orderVo.setOrderItemVoList(orderItemVoList);
        return CommonResponse.createForSuccess(orderVo);
    }

    @Override
    public CommonResponse<Page<OrderVO>> getOrderList(int pageNum, int pageSize, Integer userId) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", userId);
        page = orderMapper.selectPage(page, orderQueryWrapper);
        List<Order> orderList = page.getRecords();
        List<OrderVO> orderVoList = Lists.newArrayList();
        //使用ListBeanUtils
//        List<OrderVo> orderVoList = ListBeanUtils.copyListProperties(orderList, OrderVo::new,(Order,OrderVo)->{
//            OrderVo.setAddressVO(addressService.findAddress(Order.getAddressId()).getData());
//            QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
//            orderItemQueryWrapper.eq("order_no", Order.getOrderNo());
//            List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);
//            List<OrderItemVo> orderItemVoList = ListBeanUtils.copyListProperties(orderItemList, OrderItemVo::new);
//            OrderVo.setOrderItemVoList(orderItemVoList);
//            OrderVo.setImageServer(imageServerConfig.getUrl());
//        });
        for (Order order : orderList) {
            OrderVO orderVo = new OrderVO();
            BeanUtils.copyProperties(order, orderVo);
            orderVo.setAddressVO(addressService.findAddress(order.getAddressId()).getData());
            orderVo.setImageServer(imageServerConfig.getUrl());
            QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
            orderItemQueryWrapper.eq("order_no", order.getOrderNo());
            List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);
            List<OrderItemVO> orderItemVoList = ListBeanUtils.copyListProperties(orderItemList, OrderItemVO::new);
            orderVo.setOrderItemVoList(orderItemVoList);
            orderVoList.add(orderVo);
        }
        Page<OrderVO> orderVoPage = new Page<>(pageNum, pageSize);
        //BeanUtils.copyProperties(page, orderVoPage);
        orderVoPage.setRecords(orderVoList);
        return CommonResponse.createForSuccess(orderVoPage);
    }

    @Override
    public CommonResponse<Object> cancelOrder(String orderNo) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        Order order = orderMapper.selectOne(orderQueryWrapper);
        if (order == null)
            return CommonResponse.createForError("订单不存在");
        order.setStatus(CONSTANT.OrderStatus.CANCELED.getCode());
        order.setCloseTime(LocalDateTime.now());
        if (orderMapper.updateById(order) > 0)
            return CommonResponse.createForSuccess("取消订单成功");
        else
            return CommonResponse.createForError("取消订单失败");
    }

    public Long generateOrderNo() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        String time = dateFormat.format(new Date());
//        String random = RandomStringUtils.randomNumeric(8);
//        String result = time + random;
//        return Long.valueOf(result);
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String localDate = LocalDateTime.now().format(ofPattern);
        //随机数
        String randomNumeric = RandomStringUtils.randomNumeric(8);
        return  Long.parseLong(randomNumeric);
    }
}

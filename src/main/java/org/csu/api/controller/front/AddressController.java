package org.csu.api.controller.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.AddAddressDTO;
import org.csu.api.dto.UpdateAddressDTO;
import org.csu.api.service.AddressService;
import org.csu.api.service.UserService;
import org.csu.api.vo.AddressVO;
import org.csu.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address/")
@CrossOrigin(originPatterns = "*",allowCredentials = "true")
@Validated
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;
    @PostMapping("/add")
    public CommonResponse<AddressVO> addAddress(@Valid @RequestBody AddAddressDTO addAddressDTO, HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.addAddress(userVO.getId(), addAddressDTO);
    }
    @PostMapping("/delete")
    public CommonResponse<Object> deleteAddress(@Valid @RequestBody Integer addressId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.deleteAddress(addressId);
    }
    @PostMapping("/update")
    public CommonResponse<AddressVO> updateAddress(@Valid @RequestBody UpdateAddressDTO updateAddressDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.updateAddress(updateAddressDTO);
    }
    @GetMapping("/find")
    public CommonResponse<AddressVO> findAddress(@RequestParam Integer addressId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.findAddress(addressId);
    }
    @GetMapping("/list")
    public CommonResponse<List<AddressVO>> findAllAddress(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserVO userVO = userService.getUserVOByToken(token);
        if (userVO == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.findAllAddress(userVO.getId());
    }
}

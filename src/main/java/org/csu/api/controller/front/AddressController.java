package org.csu.api.controller.front;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.dto.AddAddressDTO;
import org.csu.api.dto.UpdateAddressDTO;
import org.csu.api.service.AddressService;
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
    @PostMapping("/add")
    public CommonResponse<AddressVO> addAddress(@Valid @RequestBody AddAddressDTO addAddressDTO, HttpSession session) {

        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.addAddress(loginUser.getId(), addAddressDTO);
    }
    @PostMapping("/delete")
    public CommonResponse<Object> deleteAddress(@Valid @RequestBody Integer addressId, HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.deleteAddress(addressId);
    }
    @PostMapping("/update")
    public CommonResponse<AddressVO> updateAddress(@Valid @RequestBody UpdateAddressDTO updateAddressDTO, HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.updateAddress(updateAddressDTO);
    }
    @GetMapping("/find")
    public CommonResponse<AddressVO> findAddress(@RequestParam Integer addressId, HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.findAddress(addressId);
    }
    @GetMapping("/list")
    public CommonResponse<List<AddressVO>> findAllAddress(HttpSession session) {
        UserVO loginUser = (UserVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return addressService.findAllAddress(loginUser.getId());
    }
}

package org.csu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.domain.Address;
import org.csu.api.domain.User;
import org.csu.api.dto.AddAddressDTO;
import org.csu.api.dto.UpdateAddressDTO;
import org.csu.api.persistence.AddressMapper;
import org.csu.api.persistence.UserMapper;
import org.csu.api.service.AddressService;
import org.csu.api.vo.AddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("addressService")
@Slf4j
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public CommonResponse<AddressVO> addAddress(Integer id, AddAddressDTO addAddressDTO) {
        User user = userMapper.selectById(id);
        if (user == null)
            return CommonResponse.createForError("用户不存在");

        if (isRecordExist(id, addAddressDTO)) {
            return CommonResponse.createForError("该地址已存在");
        } else {
            Address address = new Address();
            address.setUserId(id);
            BeanUtils.copyProperties(addAddressDTO, address);
            if (addressMapper.insert(address) > 0) {
                AddressVO addressVO = new AddressVO();
                BeanUtils.copyProperties(address, addressVO);
                return CommonResponse.createForSuccess(addressVO);
            } else
                return CommonResponse.createForError("添加地址失败");
        }

    }

    @Override
    public CommonResponse<Object> deleteAddress(Integer addressId) {
        if (addressMapper.deleteById(addressId) > 0)
            return CommonResponse.createForSuccess(ResponseCode.SUCCESS.getDescription());
        else
            return CommonResponse.createForError("删除地址失败");

    }

    @Override
    public CommonResponse<AddressVO> updateAddress(UpdateAddressDTO updateAddressDTO) {
        Address address = addressMapper.selectById(updateAddressDTO.getId());
        if (address == null)
            return CommonResponse.createForError("地址不存在");
        BeanUtils.copyProperties(updateAddressDTO, address);
        if (addressMapper.updateById(address) > 0) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(address, addressVO);
            return CommonResponse.createForSuccess(addressVO);
        } else
            return CommonResponse.createForError("更新地址失败");


    }

    @Override
    public CommonResponse<AddressVO> findAddress(Integer addressId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null)
            return CommonResponse.createForError("地址不存在");
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(address, addressVO);
        return CommonResponse.createForSuccess(addressVO);

    }

    @Override
    public CommonResponse<List<AddressVO>> findAllAddress(Integer id) {
        QueryWrapper<Address> AddressQueryWrapper = new QueryWrapper<>();
        AddressQueryWrapper.eq("user_id", id);
        List<Address> addressList = addressMapper.selectList(AddressQueryWrapper);
        if (addressList == null)
            return CommonResponse.createForError("地址不存在");
        List<AddressVO> addressVOList = new ArrayList<>();
        for (Address address : addressList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(address, addressVO);
            addressVOList.add(addressVO);
        }
        return CommonResponse.createForSuccess(addressVOList);

    }

    public boolean isRecordExist(Integer userId, AddAddressDTO addAddressDTO) {
        QueryWrapper<Address> AddressQueryWrapper = new QueryWrapper<>();
        AddressQueryWrapper.eq("user_id", userId)
                .eq("address_name", addAddressDTO.getAddressName())
                .eq("address_phone", addAddressDTO.getAddressPhone())
                .eq("address_mobile", addAddressDTO.getAddressMobile())
                .eq("address_province", addAddressDTO.getAddressProvince())
                .eq("address_city", addAddressDTO.getAddressCity())
                .eq("address_district", addAddressDTO.getAddressDistrict())
                .eq("address_detail", addAddressDTO.getAddressDetail())
                .eq("address_zip", addAddressDTO.getAddressZip());
        Address address = addressMapper.selectOne(AddressQueryWrapper);
        if (address != null) {
            return true;
        } else
            return false;
    }
}

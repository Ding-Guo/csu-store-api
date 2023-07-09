package org.csu.api.service;

import org.csu.api.common.CommonResponse;
import org.csu.api.dto.AddAddressDTO;
import org.csu.api.dto.UpdateAddressDTO;
import org.csu.api.vo.AddressVO;

import java.util.List;

public interface AddressService {
    CommonResponse<AddressVO> addAddress(Integer id, AddAddressDTO addAddressDTO);

    CommonResponse<Object> deleteAddress(Integer addressId);

    CommonResponse<AddressVO> updateAddress(UpdateAddressDTO updateAddressDTO);

    CommonResponse<AddressVO> findAddress(Integer addressId);

    CommonResponse<List<AddressVO>> findAllAddress(Integer id);
}

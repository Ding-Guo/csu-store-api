package org.csu.api.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.api.domain.Address;
import org.csu.api.domain.CartItem;
import org.springframework.stereotype.Repository;

@Repository("addressMapper")
public interface AddressMapper extends BaseMapper<Address> {
}

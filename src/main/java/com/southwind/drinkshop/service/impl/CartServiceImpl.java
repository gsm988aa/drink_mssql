package com.southwind.drinkshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.drinkshop.entity.Cart;
import com.southwind.drinkshop.entity.Product;
import com.southwind.drinkshop.enums.ResultEnum;
import com.southwind.drinkshop.exception.MallException;
import com.southwind.drinkshop.mapper.CartMapper;
import com.southwind.drinkshop.mapper.ProductMapper;
import com.southwind.drinkshop.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.drinkshop.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yihong
 * @since 2021-03-06
 */
@Service
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public boolean save(Cart entity) {
        //扣库存
        Product product = productMapper.selectById(entity.getProductId());
        Integer stock = product.getStock() - entity.getQuantity();
        if (stock < 0) {
            log.error("【添加购物车】库存不足！stock={}", stock);
            throw new MallException(ResultEnum.STOCK_ERROR);
        }
        product.setStock(stock);
        productMapper.updateById(product);
        if (cartMapper.insert(entity) == 1) {
            return true;
        }
        return false;
    }


    @Override
    public List<CartVO> findAllCartVOByUserId(Integer id) {
        List<CartVO> cartVOList = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",id);
        List<Cart> cartList = cartMapper.selectList(wrapper);
        for (Cart cart : cartList) {
            CartVO cartVO = new CartVO();
            Product product = productMapper.selectById(cart.getProductId());
            BeanUtils.copyProperties(product,cartVO);
            BeanUtils.copyProperties(cart,cartVO);
            cartVOList.add(cartVO);
        }
        return cartVOList;
    }

}

package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Cart;
import com.example.shop.domain.shop.Discount;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.ItemSize;
import com.example.shop.dto.cart.CartItemDto;
import com.example.shop.dto.cart.CartRequestDto;
import com.example.shop.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ValidationService validationService;

    @Transactional
    public boolean saveOrUpdateCart(Long memberId, CartRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);
        Item item = validationService.validateItemById(dto.getItemId());
        ItemSize itemSize = item.getItemSizeBySize(dto.getSize());

        List<Cart> carts = cartRepository.findCartDetails(memberId, itemSize.getId());
        Cart existCart = carts.isEmpty() ? null : carts.get(0);

        if (itemSize.getStockQuantity() < dto.getQuantity()) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }

        if (existCart != null) {
            existCart.updateQuantity(existCart.getQuantity() + dto.getQuantity());
            return false;
        } else {
            Cart cart = Cart.createCart(member, itemSize, dto.getQuantity());
            cartRepository.save(cart);
            return true;
        }
    }

    @Transactional
    public void updateCart(Long cartId, int quantity) {
        Cart cart = validationService.validateCartById(cartId);

        if (cart.getItemSize().getStockQuantity() < quantity) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }

        cart.updateQuantity(quantity);
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> getCarts(Long memberId) {
        List<Cart> carts = cartRepository.findCartDetails(memberId);

        return carts.stream()
                .map(cart -> {
                    Discount discount = validationService.findDiscountByItemId(cart.getItemSize().getItem().getId());
                    int price = cart.getItemSize().getItem().getPrice();
                    if (discount != null) {
                        price = discount.getDiscountPrice();
                    }
                    return CartItemDto.createCartItemDto(cart.getId(), cart.getItemSize().getId(),
                            cart.getItemSize().getItem().getName(), price,
                            cart.getItemSize().getItem().getRepItemImage(), cart.getQuantity(),
                            price * cart.getQuantity());
                })
                .toList();
    }

    @Transactional
    public void deleteCartItems(Long memberId, List<Long> cartItemIds) {
        if (cartItemIds.isEmpty()) {
            throw new IllegalArgumentException("아무 상품도 선택되지 않았습니다.");
        }
        cartRepository.deleteAllByMemberIdAndIdIn(memberId, cartItemIds);
    }
}

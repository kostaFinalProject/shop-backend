package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.*;
import com.example.shop.dto.cart.CartItemDto;
import com.example.shop.dto.cart.CartRequestDto;
import com.example.shop.dto.order.OrderItemRequestDto;
import com.example.shop.dto.order.OrderRequestDto;
import com.example.shop.repository.cart.CartRepository;
import com.example.shop.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ValidationService validationService;

    public boolean saveOrUpdateCart(Long memberId, CartRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);
        Item item = validationService.validateItemById(dto.getItemId());
        ItemSize itemSize = item.getItemSizeBySize(dto.getSize());

        Optional<Cart> existingCart = cartRepository.findByMemberIdAndItemSizeId(memberId, itemSize.getId());

        if (itemSize.getStockQuantity() < dto.getQuantity()) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }

        if (existingCart.isPresent()) {
            existingCart.get().updateQuantity(existingCart.get().getQuantity() + dto.getQuantity());
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
                            cart.getItemSize().getSize().getSize(), cart.getItemSize().getItem().getName(), price,
                            cart.getItemSize().getItem().getRepItemImage(), cart.getQuantity(),
                            price * cart.getQuantity(), cart.getItemSize().getItem().getManufacturer(),
                            cart.getItemSize().getItem().getSeller());
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

    @Transactional
    public Order saveOrderFromCart(Long memberId, OrderRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);

        List<OrderItem> orderItems = dto.getOrderItems().stream()
                .map(orderItemRequestDto -> {
                    ItemSize itemSize = validationService.validateItemSizeById(orderItemRequestDto.getItemSizeId());
                    Discount discount = validationService.findDiscountByItemId(itemSize.getItem().getId());

                    int price = itemSize.getItem().getPrice();
                    if (discount != null) {
                        price = discount.getDiscountPrice();
                    }
                    return OrderItem.createOrderItem(itemSize, price, orderItemRequestDto.getCount());
                })
                .toList();

        Order order = Order.createOrder(member, orderItems);

        cartRepository.deleteAllByMemberIdAndItemSizeIdIn(memberId, dto.getOrderItems().stream()
                .map(OrderItemRequestDto::getItemSizeId)
                .collect(Collectors.toList()));

        return orderRepository.save(order);
    }
}

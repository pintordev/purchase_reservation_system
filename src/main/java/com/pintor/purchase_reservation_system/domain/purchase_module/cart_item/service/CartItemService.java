package com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.product_module.product.service.ProductService;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.entity.Cart;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.service.CartService;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.repository.CartItemRepository;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.request.CartItemCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartService cartService;
    private final ProductService productService;

    @Transactional
    public void create(CartItemCreateRequest request, BindingResult bindingResult, User user) {

        this.createValidate(bindingResult);
        Product product = this.productService.getProductDetail(request.getProductId());

        Cart cart = this.cartService.getCart(user);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .name(product.getName())
                .price(product.getPrice())
                .quantity(request.getQuantity())
                .build();

        this.cartItemRepository.save(cartItem);
    }

    private void createValidate(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            log.error("cart item create request validation failed: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }
    }

    public void update(Long id, CartItemCreateRequest request, BindingResult bindingResult) {
    }
}

package com.javaeat.handler.order;

import com.javaeat.enums.CartStatus;
import com.javaeat.enums.ErrorMessage;
import com.javaeat.enums.PaymentMethod;
import com.javaeat.exception.NotFoundException;
import com.javaeat.model.Cart;
import com.javaeat.payment.CardPaymentStrategy;
import com.javaeat.payment.CashPaymentStrategy;
import com.javaeat.payment.PayPalPaymentStrategy;
import com.javaeat.payment.PaymentStrategy;
import com.javaeat.repository.CartRepository;
import com.javaeat.repository.PaymentRespository;
import com.javaeat.request.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Slf4j
@AllArgsConstructor
public class PaymentProcessHandler  extends OrderHandler {

    private final PaymentRespository paymentRespository;
    private final CartRepository cartRepository;

    @Override
    public boolean handle(OrderRequest request) {
        PaymentStrategy paymentStrategy = getPaymentStrategy(request.getPaymentDetails().getMethod());

        if (!paymentStrategy.processPayment(request.getPaymentDetails())) {
            log.info("Payment failed. Unlocking the cart.");
            //unlock the cart
            unlockCart(request.getCartId());
            return false;
        }
        return handleNext(request);
    }

    private void unlockCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new NotFoundException(HttpStatus.NOT_FOUND.value(),
                        ErrorMessage.CART_NOT_FOUND.name()));

        cart.setStatus(CartStatus.READ_WRITE);
        cart.setLastUpdatedTime(LocalDateTime.now());
    }

    private PaymentStrategy getPaymentStrategy(PaymentMethod paymentType) {
        switch (paymentType) {
            case CASH:
                return new CashPaymentStrategy();
            case CARD:
                return new CardPaymentStrategy();
            case PAYPAL:
                return new PayPalPaymentStrategy();
            default:
                throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        }
    }
}
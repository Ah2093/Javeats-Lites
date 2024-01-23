package com.javaeat.controller;

import com.javaeat.request.OrderRequest;
import com.javaeat.request.OrderResponse;
import com.javaeat.response.OrderStatusResponse;
import com.javaeat.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/order")
@Slf4j
@Tag(name = "Place An Order Endpoints")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Endpoint that creates an order.",
            description = "Endpoint that creates an order.")
    @ApiResponse(responseCode = "201", description = "Order has been created Successfully")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        // a method to call the service to create the order
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order.",
            description = "Endpoint that retrieves all order details")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @ApiResponse(responseCode = "404", description = "Order Not Found Exception")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Integer orderId) {
        // a method to call the service to get the order details
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }


    @DeleteMapping("/cancel/{orderId}")
    @Operation(summary = "Endpoint that cancel order.",
            description = "Endpoint that cancel the order.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @ApiResponse(responseCode = "404", description = "Order Not Found Exception")
    public ResponseEntity<OrderStatusResponse> cancelOrder(@PathVariable Integer orderId) {
        // a method to call the service to cancel the order
        return ResponseEntity.ok(orderService.cancel(orderId));
    }

    @GetMapping("/status/{orderId}")
    @Operation(summary = "Endpoint that checks order status",
            description = "Endpoint that retrieves the current status of the order.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @ApiResponse(responseCode = "404", description = "Order Not Found Exception")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@PathVariable Integer orderId) {
        // a method to call the service to get the status

        //return a fake status
        return ResponseEntity.ok(orderService.getStatus(orderId));
    }

    @PutMapping("/status")
    @Operation(summary = "Endpoint that updates order status",
            description = "Endpoint that updates the current order of the shopping cart.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @ApiResponse(responseCode = "404", description = "Order Not Found Exception")
    public ResponseEntity<OrderStatusResponse> updateOrderStatus(@RequestParam Integer orderId) {
        // a method to call the service to update the status
        return ResponseEntity.ok(orderService.updateStatus(orderId));
    }


}

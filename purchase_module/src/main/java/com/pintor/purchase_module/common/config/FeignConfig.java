package com.pintor.purchase_module.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = {
        "com.pintor.purchase_module.api.member_module.member.client",
        "com.pintor.purchase_module.api.product_module.product.client",
        "com.pintor.purchase_module.api.product_module.stock.client",
})
@Configuration
public class FeignConfig {
}
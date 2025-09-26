// ClientProductDto.java
package com.example.HW2.client_processing.dto;

import com.example.HW2.client_processing.entity.ClientProduct;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ClientProductDto {
    private Long clientId;
    private Long productId;

    public static ClientProductDto fromEntity(ClientProduct entity) {
        return ClientProductDto.builder()
                .clientId(entity.getClientId())
                .productId(entity.getProductId())
                .build();
    }
}

package com.example.HW_2.client_processing.dto;

import com.example.HW_2.client_processing.entity.Product;
import com.example.HW_2.client_processing.enums.ProductKey;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private ProductKey key;
    private String productId;

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .key(product.getKey())
                .productId(product.getProductId())
                .build();
    }

    public Product toEntity() {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .key(this.key)
                .build();
    }
}

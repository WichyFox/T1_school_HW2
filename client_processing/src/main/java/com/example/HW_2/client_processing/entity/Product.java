package com.example.HW2.client_processing.entity;

import com.example.HW2.client_processing.enums.ProductKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_key")
    private ProductKey key;


    @Column(name = "product_id", unique = true)
    private String productId;

    //переделать, ТЗшный способ не по НФ
    @PrePersist
    public void generateProductId() {
        if (this.key != null && this.id != null) {
            this.productId = this.key.toString() + this.id.toString();
        }
    }
}

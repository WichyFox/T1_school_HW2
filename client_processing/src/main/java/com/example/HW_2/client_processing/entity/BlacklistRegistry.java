package com.example.HW2.client_processing.entity;

import com.example.HW2.client_processing.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "blacklist_registry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_id", nullable = false)
    private String documentId;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDate blacklistedAt;

    @Column(name = "reason")
    private String reason;

    @Column(name = "blacklist_expiration_date")
    private LocalDate blacklistExpirationDate;
}

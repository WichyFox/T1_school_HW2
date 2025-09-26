package com.example.HW2.client_processing.entity;

import com.example.HW2.client_processing.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*- clientId (формат clientId: XXFFNNNNNNNN, где XX - номер региона, FF - номер подразделения банка,
     N - порядковый. Например, 770100000001, 770200000023)*/
    @Column(name = "client_id", unique = true, nullable = false, length = 12)
    private String clientId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "document_prefix")
    private String documentPrefix;

    @Column(name = "document_suffix")
    private String documentSuffix;
}
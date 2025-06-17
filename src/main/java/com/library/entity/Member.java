package com.library.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Schema(description = "Member entity representing a library member")
@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Schema(description = "Unique identifier of the member", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    
    @Schema(description = "Full name of the member", example = "John Smith", required = true)
    @Column(nullable = false)
    private String name;
    
    @Schema(description = "Email address of the member", example = "john.smith@email.com", required = true)
    @Column(nullable = false, unique = true)
    private String email;
    
    @Schema(description = "Phone number of the member", example = "+1-555-0101")
    private String phone;
    
    @Schema(description = "Address of the member", example = "123 Main St, Anytown, USA")
    private String address;
    
    @Schema(description = "Current membership status", example = "ACTIVE", allowableValues = {"ACTIVE", "SUSPENDED", "EXPIRED"})
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
    
    @Schema(description = "Date when the member registered", example = "2024-01-15", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate registrationDate = LocalDate.now();
    
    public enum MembershipStatus {
        ACTIVE, SUSPENDED, EXPIRED
    }
}

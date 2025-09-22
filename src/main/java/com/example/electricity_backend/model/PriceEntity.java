package com.example.electricity_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prices",
       uniqueConstraints = @UniqueConstraint(columnNames = {"start_time", "resolution_minutes"})) // Ensure no duplicate entries for the same start time and resolution
@Getter
@Setter
@NoArgsConstructor
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "price_cents", nullable = false)
    private BigDecimal price;

    @Column(name = "resolution_minutes", precision = 10, scale = 3, nullable = false)
    private int resolutionMinutes;
}

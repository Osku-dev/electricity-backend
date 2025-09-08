package com.example.electricity_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "prices")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "price_cents", nullable = false)
    private BigDecimal price;

    @Column(name = "resolution_minutes", nullable = false)
    private int resolutionMinutes;
}

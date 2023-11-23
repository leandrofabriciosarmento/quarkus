package com.example.quarkus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDTO(
        @NotBlank String name,
        @NotNull BigDecimal unitValue) {
}

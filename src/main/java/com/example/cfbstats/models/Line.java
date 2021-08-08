package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Line {
    private String provider;
    private BigDecimal spread;
    private String formattedSpread;
    private BigDecimal overUnder;
}

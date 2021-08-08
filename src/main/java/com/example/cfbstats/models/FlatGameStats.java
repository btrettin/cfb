package com.example.cfbstats.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlatGameStats {
    @JsonSerialize(using = NumericBooleanSerializer.class)
    private boolean homewin;
    private String homename;
    private String awayname;
    private BigDecimal talentDifference;
    private BigDecimal scoreMarginDifference;
    private BigDecimal eloDifference;
    private BigDecimal spread;
}

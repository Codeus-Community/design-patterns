package org.codeus.design_patterns.cor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class BankRequest {
    private String requestId;
    private String type;
    @Setter
    private double amount;
    private String userId;
}

package com.example.Transaction.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class productRequest     {
    private Long amount;
    private String name;
    private String currency;
    private Long quantity;
}

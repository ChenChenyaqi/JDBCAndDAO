package com.myCompany.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author chenyaqi
 * @date 2021/2/25 - 14:25
 */
@Getter
@Setter
public class Customer {
    private int id;
    private String name;
    private BigDecimal balance;

    public Customer(int id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        return "id: "+ id + ", name: " + name + ", balance: " + balance ;
    }
}

package com.driver;

public class OrderIdNotPresentException extends RuntimeException{
    public OrderIdNotPresentException(String str)
    {
        super(str);
    }
}

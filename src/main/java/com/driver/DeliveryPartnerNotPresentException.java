package com.driver;

public class DeliveryPartnerNotPresentException extends RuntimeException{
    public DeliveryPartnerNotPresentException(String str)
    {
        super(str);
    }
}

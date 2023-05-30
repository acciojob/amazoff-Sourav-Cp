package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Map<String,Order> orderMap = new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap = new HashMap<>();
    Map<String , ArrayList<String>> partnerOrderMap = new HashMap<>();
    Map<String,String> orderPartnerMap = new HashMap<>();

    public void addOrder(Order order)
    {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(DeliveryPartner deliveryPartner)
    {
        deliveryPartnerMap.put(deliveryPartner.getId(),deliveryPartner);
    }

    public Optional<Order> getOrderById(String orderId)
    {
        if(orderMap.containsKey(orderId))
        {
            return Optional.of(orderMap.get(orderId));
        }
        return Optional.empty();
    }

    public Optional<DeliveryPartner> getDeliveryPartnerById(String partnerId)
    {
        if(deliveryPartnerMap.containsKey(partnerId))
        {
            return Optional.of(deliveryPartnerMap.get(partnerId));
        }
        return Optional.empty();
    }
    public void addOrderPartnerPair(String orderId,String partnerId)
    {
        ArrayList<String> orders = partnerOrderMap.getOrDefault(partnerId,new ArrayList<>());

        orders.add(orderId);
        partnerOrderMap.put(partnerId,orders);
        orderPartnerMap.put(orderId,partnerId);
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        return partnerOrderMap.getOrDefault(partnerId,new ArrayList<>());
    }
    public List<String> getAllOrders()
    {
        return new ArrayList<>(orderMap.keySet());
    }
    public List<String> getAssignedOrders()
    {
        return new ArrayList<>(orderPartnerMap.keySet());
    }
    public void deletePartner(String partnerId)
    {
        deliveryPartnerMap.remove(partnerId);
        partnerOrderMap.remove(partnerId);
    }
    public void UnassignedOrder(String Id)
    {
        orderPartnerMap.remove(Id);
    }

    public String getOrderPartner(String orderId)
    {
        return orderPartnerMap.get(orderId);
    }
    public void deleteOrder(String orderId)
    {
        orderPartnerMap.remove(orderId);
        orderMap.remove(orderId);
    }
}

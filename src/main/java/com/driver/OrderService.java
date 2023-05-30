package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order)
    {
        orderRepository.addOrder(order);
    }
    public void addPartner(String partnerId)
    {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        orderRepository.addPartner(deliveryPartner);
    }
    public void addOrderPartnerPair(String orderId,String partnerId) throws DeliveryPartnerNotPresentException,OrderIdNotPresentException
    {
        Optional<Order> order = orderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> deliveryPartner = orderRepository.getDeliveryPartnerById(partnerId);

        if(order.isEmpty())
        {
            throw new OrderIdNotPresentException("Order id not found :" + orderId);
        }
        if(deliveryPartner.isEmpty())
        {
            throw new DeliveryPartnerNotPresentException("Delivery partner not found :" +partnerId);
        }

        DeliveryPartner partner = deliveryPartner.get();
        partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
        orderRepository.addPartner(partner);

        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }
    public Order getOrderById(String orderId) throws OrderIdNotPresentException
    {
        Optional<Order> optionalOrder = orderRepository.getOrderById(orderId);
        if(optionalOrder.isPresent()) return optionalOrder.get();

        throw new OrderIdNotPresentException("Order id not found :" + orderId);
    }
    public DeliveryPartner getPartnerByPartnerId(String partnerId) throws DeliveryPartnerNotPresentException
    {
        Optional<DeliveryPartner> optionalDeliveryPartner = orderRepository.getDeliveryPartnerById(partnerId);
        if(optionalDeliveryPartner.isPresent()) return optionalDeliveryPartner.get();

        throw new DeliveryPartnerNotPresentException("Delivery partner id not found :" + partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId)
    {
        Optional<DeliveryPartner> optionalDeliveryPartner = orderRepository.getDeliveryPartnerById(partnerId);
        if(optionalDeliveryPartner.isPresent())
        {
            return optionalDeliveryPartner.get().getNumberOfOrders();
        }
        return 0;
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }
    public List<String> getAllOrders()
    {
        return orderRepository.getAllOrders();
    }
    public int getCountOfUnassignedOrders()
    {
        return orderRepository.getAllOrders().size() - orderRepository.getAssignedOrders().size();
    }

    public int convertDeliveryTime(String deliveryTime)
    {
        String [] time = deliveryTime.split(":");
        return Integer.parseInt(time[0])*60 + Integer.parseInt(time[1]);
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId)
    {
        List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
        List<Order> orders = new ArrayList<>();

        for(String Id : orderIds)
        {
            Order order = orderRepository.getOrderById(Id).get();
            if(order.getDeliveryTime() > convertDeliveryTime(time) )
            {
                orders.add(order);
            }
        }
        return orders.size();
    }
    public String convertDeliveryTime(int deliveryTime)
    {
        int hh = deliveryTime/60;
        int mm = deliveryTime%60;

        String HH = String.valueOf(hh);
        String MM = String.valueOf(mm);

        return String.format("%s:%s",HH,MM);
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
        int max = 0;

        for(String orders : orderIds)
        {
            Order order = orderRepository.getOrderById(orders).get();
            if(order.getDeliveryTime() > max)
            {
                max = order.getDeliveryTime();
            }
        }
        return convertDeliveryTime(max);
    }
    public void deletePartnerById(String partnerId)
    {
        List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
        orderRepository.deletePartner(partnerId);

        for(String Id : orderIds)
        {
            orderRepository.UnassignedOrder(Id);
        }
    }
    public void deleteOrderById(String orderId)
    {
       String partnerId = orderRepository.getOrderPartner(orderId);
       orderRepository.deleteOrder(orderId);

       if(Objects.nonNull(partnerId)) {
           List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
           orderIds.remove(orderId);
       }
    }
}

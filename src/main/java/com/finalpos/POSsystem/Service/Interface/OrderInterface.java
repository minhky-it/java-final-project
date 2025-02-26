package com.finalpos.POSsystem.Service.Interface;

import java.util.Map;

public interface OrderInterface {
    Map<String, Object> ordersByOrdNumber(String order_number);
    Map<String, Object> orders(int page, int size);
}

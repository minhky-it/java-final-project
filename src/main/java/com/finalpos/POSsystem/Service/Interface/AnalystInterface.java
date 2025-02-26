package com.finalpos.POSsystem.Service.Interface;

import java.util.Map;

public interface AnalystInterface {
    Map<String, Object> orders(String token);
    Map<String, Object> ordersByDate(String startDate, String endDate, String token);
}

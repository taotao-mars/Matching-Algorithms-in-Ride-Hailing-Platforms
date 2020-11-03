package com.company;

import java.util.*;

public class Customer_demand  {

    List<Trip> trip_list;
    int k_i;
    Customer_demand(){
        trip_list=new ArrayList<>();
        k_i=0;
    }

    public int getK_i() {
        return k_i;
    }

    public void setK_i(int k_i) {
        this.k_i = k_i;
    }
}

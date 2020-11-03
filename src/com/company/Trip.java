package com.company;


import java.io.Serializable;

import static com.company.TripSchedulingAlgorithm.calculate_distance;

public class Trip implements Serializable {

    public int trip_id;
    public int shared_with;
    private Point src;
    private Point Dest;
    private int startTime;
    private int distance_can_be_charged;
    private double revenue;
    private double cost_trip;
    private double Customer_satisfaction_penalty;
    private double timestamp_picked_up;
    private int car_assigned_id;
    private double end_time;
    private double waiting_time;
    public double Gasoline_cost_rate=0.003;
    public double Electricity_cost_rate=0.0005;
    public double Normal_charge_rate=0.016;
    public double Shared_charge_rate=0.0129;


    public double getPick_up_waiting_time() {
        return pick_up_waiting_time;
    }

    private double pick_up_waiting_time;

    public int getCar_assigned_id() {
        return car_assigned_id;
    }

    public void setCar_assigned_id(int car_assigned_id) {
        this.car_assigned_id = car_assigned_id;
    }

    public double getTimestamp_picked_up() {
        return timestamp_picked_up;
    }

    public void setTimestamp_picked_up(double pick_up_waiting_time,double last_trip_remain_time) {
        this.pick_up_waiting_time=pick_up_waiting_time;
        this.waiting_time=pick_up_waiting_time+last_trip_remain_time;
        this.timestamp_picked_up = waiting_time+startTime;
    }
    public void calculate_end_timestamp(Car car){
        this.end_time=timestamp_picked_up+distance_can_be_charged/car.speed;
    }


    public double getRevenue() {
        return revenue;
    }

    public int getDistance_can_be_charged() {
        return distance_can_be_charged;
    }



    public Point getSrc() {
        return src;
    }

    public void setSrc(Point src) {
        this.src = src;
    }

    public Point getDest() {
        return Dest;
    }

    public void setDest(Point dest) {
        Dest = dest;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public double getCost_trip() {
        return cost_trip;
    }

    public void setCost_trip(double cost_trip) {
        this.cost_trip = cost_trip;
    }

    public double getEnd_time() {
        return end_time;
    }

    public void setEnd_time(double end_time) {
        this.end_time = end_time;
    }

    public void calculate_penalty() {
        if(waiting_time<900){
            Customer_satisfaction_penalty=0;
        }
        else {
            Customer_satisfaction_penalty=(waiting_time-900)/900;
        }

    }
    public void calculate_cost_and_revenue_and_set_car_distance_traveled(double time_cost_on_way, Car car) {
        int Cost_distance=(int)(time_cost_on_way*car.speed)+distance_can_be_charged;
        if(!car.is_electronic){
            this.cost_trip= Cost_distance* Gasoline_cost_rate;
        }else {
            this.cost_trip = Cost_distance* Electricity_cost_rate;
        }
        revenue=Math.max(2.20+Normal_charge_rate*distance_can_be_charged,7.2);
        car.Distance_traveled+=Cost_distance;
    }
    public void calculate_revenue_shared(){
        revenue=Math.max(2.20+Shared_charge_rate*distance_can_be_charged,7.2);
    }

    public void calculate_cost_shared( int total_distance_for_this_shared_trip,Car car){
        if(!car.is_electronic){
            this.cost_trip= total_distance_for_this_shared_trip/2.0* Gasoline_cost_rate;
        }else {
            this.cost_trip = total_distance_for_this_shared_trip/2.0* Electricity_cost_rate;
        }
    }

    public double getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(int waiting_time) {
        this.waiting_time = waiting_time;
    }

    public double getCustomer_penalty() {
        return Customer_satisfaction_penalty;
    }

    Trip(){

    }

    Trip(Point src, Point Dest, int startTime,int trip_id){
        this.src=src;
        this.Dest=Dest;
        this.startTime=startTime;
        cost_trip=0;
        distance_can_be_charged=calculate_distance(src,Dest);
        end_time=0;
        waiting_time=0;
        shared_with=-1;
        car_assigned_id=-1;
        this.trip_id=trip_id;
    }

}

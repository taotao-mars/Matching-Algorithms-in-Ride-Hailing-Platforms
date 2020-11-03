package com.company;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.company.AdditionalFunctions.*;

public class GreedySchedulingAlgorithmPlusWaitingTime_version2 extends TripSchedulingAlgorithm implements TripSchedulingAlgorithm.algorithm {

    public static int TripSchedule(Car_list car_list, Trip trip,int max_waiting_time){
        int size=car_list.car_list.size();//number of cars
        double[] a=new double[size];//time_from_finishing_point_to_customer
        int Distance_cost_on_way=0;
        double last_trip_remain_time=0;
        int temp=0;
        int sign=0;
        Car car=new Car();
        for(int i=0;i<size;i++){
            car=car_list.car_list.get(i);
            Distance_cost_on_way=calculate_distance(car.position,trip.getSrc());
            a[i]=Distance_cost_on_way/car.speed;
        }
        int[] index=sort_array(a);
        while(temp<size&&sign==0){
            last_trip_remain_time=Math.max(car_list.car_list.get(index[temp]).getTime_stamp_arrive_des()-trip.getStartTime(),0);
            if(last_trip_remain_time+a[temp]<max_waiting_time){
                sign=1;
                temp--;
            }
            temp++;
        }
        if(sign==0){
           // System.out.println("Can not serve this customer, because there is not a car available in "+ max_waiting_time+"s.");
            return -1;
        }
        car=car_list.car_list.get(index[temp]);
        trip.setTimestamp_picked_up(a[temp],last_trip_remain_time);
        trip.setCar_assigned_id(index[temp]);
        trip.calculate_end_timestamp(car);
        trip.calculate_penalty();
        trip.calculate_cost_and_revenue_and_set_car_distance_traveled(a[temp],car);
        car.setTime_stamp_arrive_des(trip.getEnd_time());
        car.setPosition(trip.getDest());
        return index[temp];
    }
    static void simulate(Car_list car_list, Customer_demand demand, int n, int max_waiting_time,int k_i){
        Trip trip_next=new Trip();
        int index=0;//the index of customer
        double total_cost=0;
        double total_revenue=0;
        int car_index=-1;
        int k=car_list.car_list.size();
        double Penalty=0;
        int Count=0;
        List<Trip> Trips_not_arranged=new ArrayList<>();
        Trip trip_next_not_arranged=new Trip();
        Write_content("output.csv",",");
        for(int time=0;time<3600*10;time++){
            if(time%60==0) {
                for (int j = 0; j < Trips_not_arranged.size(); j++) {
                    trip_next_not_arranged=Trips_not_arranged.get(j);
                    trip_next_not_arranged.setStartTime(time);
                    car_index = TripSchedule(car_list,trip_next_not_arranged, max_waiting_time);
                    if (car_index != -1) {
                        total_cost += trip_next_not_arranged.getCost_trip();
                        total_revenue += trip_next_not_arranged.getRevenue();
                        Penalty += trip_next_not_arranged.getCustomer_penalty();
                        Trips_not_arranged.remove(trip_next_not_arranged);
                        Count++;
                    }
                }
            }
            if(index<n) {
                trip_next=demand.trip_list.get(index);
            }
            if(trip_next.getStartTime()==time){
                index++;
                 car_index=TripSchedule(car_list,trip_next,max_waiting_time);
                if(car_index==-1){
                    Trips_not_arranged.add(trip_next);
                }else{
                        total_cost += trip_next.getCost_trip();
                        total_revenue += trip_next.getRevenue();
                        Penalty+=trip_next.getCustomer_penalty();
                        Count++;
                }
            }
        }

        double Average_revenue=total_revenue/Count;
        BigDecimal b   =   new BigDecimal(total_revenue-total_cost-Penalty*Average_revenue-(n-Count)*k_i);
        double Objective=b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        Write_content("output.csv",Objective+"");


    }
}

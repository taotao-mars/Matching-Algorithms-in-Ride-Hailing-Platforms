package com.company;
import jdk.nashorn.internal.codegen.ClassEmitter;
import sun.security.krb5.internal.crypto.dk.DkCrypto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import static com.company.AdditionalFunctions.*;


public class GreedySchedulingAlgorithmPlusWaitingTime_version1_Elec extends TripSchedulingAlgorithm implements TripSchedulingAlgorithm.algorithm {

    public static int TripSchedule(Car_list car_list, Trip trip, int max_waiting_time){
        int size=car_list.car_list.size();//number of cars
        int e_size=(int)(size*0.3);
        double[] a=new double[size-e_size];//distance between each car and the customer
        double[] e_a=new double[e_size];
        int Distance_cost_on_way=0;
        int temp=0;
        int sign=0;
        int e_temp=0;
        int e_sign=0;
        int Distance_trip=trip.getDistance_can_be_charged();
        int car_index=0;
        double pick_up_waiting_time=0;
        Car car=new Car();
        for(int i=0;i<e_size;i++){
            car=car_list.car_list.get(i);
            Distance_cost_on_way=calculate_distance(car.position,trip.getSrc());
            if(Distance_trip+Distance_cost_on_way>1000){
                e_a[i]=-1;
                continue;
            }
            e_a[i]=Distance_cost_on_way/car.speed;
        }
        for(int i=e_size;i<size;i++){
            car=car_list.car_list.get(i);
            Distance_cost_on_way=calculate_distance(car.position,trip.getSrc());
            a[i-e_size]=Distance_cost_on_way/car.speed;
        }

        int[] index=sort_array(a);
        int[] e_index=sort_array(e_a);
        while(temp<size-e_size){
            if(a[temp]<max_waiting_time&&car_list.car_list.get(index[temp]+e_size).getTime_stamp_arrive_des()<=trip.getStartTime()){
                sign=1;
                break;
            }
            temp++;
        }
        while(e_temp<e_size){
            if(e_a[e_temp]>0&&e_a[e_temp]<max_waiting_time&&car_list.car_list.get(e_index[e_temp]).getTime_stamp_arrive_des()<=trip.getStartTime()){
                e_sign=1;
                break;
            }
            e_temp++;
        }
        if(sign==0&&e_sign==0){
            return -1;
        }else if(sign==1&&e_sign==1){
            if(+e_a[e_temp]-a[temp]>600){
                car_index=index[temp]+e_size;
                pick_up_waiting_time=a[temp];
            }else{
                car_index=e_index[e_temp];
                pick_up_waiting_time=e_a[e_temp];
            }
        }else{
            if(sign==1){
                car_index=index[temp]+e_size;
                pick_up_waiting_time=a[temp];
            }else{
                car_index=e_index[e_temp];
                pick_up_waiting_time=e_a[e_temp];
            }
        }
        car=car_list.car_list.get(car_index);
        trip.setTimestamp_picked_up(pick_up_waiting_time,0);
        trip.setCar_assigned_id(car_index);
        trip.calculate_end_timestamp(car);
        trip.calculate_penalty();
        trip.calculate_cost_and_revenue_and_set_car_distance_traveled(pick_up_waiting_time,car);
        car.setTime_stamp_arrive_des(trip.getEnd_time());
        car.setPosition(trip.getDest());
        return car_index;
    }

    static void simulate( Car_list car_list, Customer_demand demand, int n, int max_waiting_time,int k_i){
        double total_cost=0;
        int k=car_list.car_list.size();
        int index=0;//the index of customer
        double total_revenue=0;
        double Count=0;
        double Penalty=0;
        Trip trip_next=new Trip();
        int car_index=-1;
        List<Trip> Trips_not_arranged=new ArrayList<>();
        Trip trip_next_not_arranged=new Trip();
        //  Write_content("output.csv","Electric Car,\""+k+","+n+"\""+","+k+","+n+",");
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
        Write_content("output.txt","Acceptance Rate "+ (double)Count/(double)n*100+ "%"+"\n");
    }

}


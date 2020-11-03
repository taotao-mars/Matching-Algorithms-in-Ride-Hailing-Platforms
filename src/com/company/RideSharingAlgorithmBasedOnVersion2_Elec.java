package com.company;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.company.AdditionalFunctions.*;

public class RideSharingAlgorithmBasedOnVersion2_Elec extends TripSchedulingAlgorithm implements TripSchedulingAlgorithm.algorithm {

    public static int TripSchedule(Car_list car_list, Customer_demand demand,int trip_index,int Last_trip_arranged_index,int max_waiting_time){
        Trip trip=demand.trip_list.get(trip_index);
        Trip trip_temp=new Trip();
        double Over_head1=0;
        double Over_head2=0;
        double Over_head1_target=1.3;
        double Over_head2_target=1.4;
        int Trip_to_be_shared=-1;
        int distance_from_A_to_C_passing_B=0;
        int distance_from_B_to_D_passing_C=0;
        int distance_from_A_to_C=0;
        int distance_from_B_to_D=0;
        for(int i=0;i<=Last_trip_arranged_index;i++){
           trip_temp=demand.trip_list.get(i);
            if(trip_temp.getCar_assigned_id()!=-1&&trip_temp.getTimestamp_picked_up()>trip.getStartTime()&&trip_temp.shared_with==-1){
                distance_from_A_to_C_passing_B=calculate_distance(trip_temp.getSrc(),trip.getSrc(),trip_temp.getDest());
                distance_from_B_to_D_passing_C=calculate_distance(trip.getSrc(),trip_temp.getDest(),trip.getDest());
                distance_from_A_to_C=calculate_distance(trip_temp.getSrc(),trip_temp.getDest());
                distance_from_B_to_D=calculate_distance(trip.getSrc(),trip.getDest());
                Over_head1=(double) distance_from_A_to_C_passing_B/distance_from_A_to_C;
                Over_head2=(double) distance_from_B_to_D_passing_C/distance_from_B_to_D;
                if(Over_head1<=1.3&&Over_head2<=1.4&&Over_head1+Over_head2<Over_head1_target+Over_head2_target){
                    Over_head1_target=Over_head1;
                    Over_head2_target=Over_head2;
                    Trip_to_be_shared=i;
                }
            }
        }
        if(Trip_to_be_shared!=-1){
            trip_temp=demand.trip_list.get(Trip_to_be_shared);
            trip.setTimestamp_picked_up(trip_temp.getTimestamp_picked_up()-trip.getStartTime()+calculate_distance(trip_temp.getSrc(),trip.getSrc()),0);
            trip.setCar_assigned_id(trip_temp.getCar_assigned_id());
            Car car =car_list.car_list.get(trip.getCar_assigned_id());
            trip.shared_with=trip_temp.trip_id;
            trip_temp.shared_with=trip.trip_id;
            trip.calculate_revenue_shared();
            trip_temp.calculate_revenue_shared();
            distance_from_A_to_C_passing_B=calculate_distance(trip_temp.getSrc(),trip.getSrc(),trip_temp.getDest());
            int distance_pick_up_A=(int)(trip_temp.getPick_up_waiting_time()*car.speed);
            int distance_from_C_to_D=calculate_distance(trip_temp.getDest(),trip.getDest());
            int total_distance_for_this_shared_trip=distance_pick_up_A+distance_from_C_to_D+distance_from_A_to_C_passing_B;
            car.Distance_traveled-=trip_temp.getCost_trip()/trip_temp.Gasoline_cost_rate;
            car.Distance_traveled+=total_distance_for_this_shared_trip;
            trip.calculate_cost_shared(total_distance_for_this_shared_trip,car);
            trip_temp.calculate_cost_shared(total_distance_for_this_shared_trip,car);
            trip_temp.setEnd_time((distance_from_A_to_C_passing_B+distance_pick_up_A)/car.speed+trip_temp.getStartTime());
            trip.setEnd_time(trip_temp.getEnd_time()+distance_from_C_to_D/car.speed);
            car.setTime_stamp_arrive_des(trip.getEnd_time());
            car.setPosition(trip.getDest());
            trip.calculate_penalty();
            return trip.getCar_assigned_id();
        }else {
          return GreedySchedulingAlgorithmPlusWaitingTime_version2_Elec.TripSchedule(car_list,trip,max_waiting_time);
        }
    }
    static void simulate(Car_list car_list, Customer_demand demand, int n, int max_waiting_time,int k_i){
        Trip trip_next=new Trip();
        int index=0;//the index of customer
        double total_cost=0;
        double total_revenue=0;
        double Penalty=0;
        int k=car_list.car_list.size();
        int Count=0;
        List<Trip> Trips_not_arranged=new ArrayList<>();
        Trip trip_next_not_arranged=new Trip();
        int car_index=-1;
        Write_content("output.csv",",");
        for(int time=0;time<3600*10;time++){
            if(index<n) {
                if(time%60==0) {
                    for (int j = 0; j < Trips_not_arranged.size(); j++) {
                        trip_next_not_arranged=Trips_not_arranged.get(j);
                        trip_next_not_arranged.setStartTime(time);
                        car_index = TripSchedule(car_list,demand,trip_next_not_arranged.trip_id,index,max_waiting_time);
                        if (car_index != -1) {
                            Trips_not_arranged.remove(trip_next_not_arranged);
                        }
                    }
                }
                trip_next=demand.trip_list.get(index);
            }else{
                break;
            }
            if(trip_next.getStartTime()==time){
                car_index=TripSchedule(car_list,demand,index,index-1,max_waiting_time);
                index++;
                if(car_index==-1){
                    Trips_not_arranged.add(trip_next);
                }
            }
        }
        for(int i=0;i<n;i++){
            trip_next=demand.trip_list.get(i);
            if(trip_next.getCar_assigned_id()!=-1){
                total_cost+=trip_next.getCost_trip();
                total_revenue+=trip_next.getRevenue();
                Penalty+=trip_next.getCustomer_penalty();
                Count++;
            }
        }
//       Write_content("output.txt","Total Cost: "+total_cost+"\n");
//       Write_content("output.txt","Total revenue: "+total_revenue+"\n");
//       Write_content("output.txt","Total customer: "+Count+"\n");

        double Average_revenue=total_revenue/Count;
        BigDecimal b   =   new BigDecimal(total_revenue-total_cost-Penalty*Average_revenue-(n-Count)*k_i);
        double Objective=b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        Write_content("output.csv",Objective+"");


    }

}


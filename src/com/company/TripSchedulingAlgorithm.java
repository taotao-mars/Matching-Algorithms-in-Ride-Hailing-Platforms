package com.company;

import java.util.HashSet;
import java.util.Vector;

import static com.company.AdditionalFunctions.*;


public abstract class TripSchedulingAlgorithm {
    interface algorithm{
        public static void simulate(Car_list car_list, Point a, Point b, int time) {

        }
        public static int TripSchedule(Car_list car_list, Point a, Point b, int time) { return 0; }
    }


    /**
     * return a distance between two points
     * @return
     */
    public static int calculate_distance(Point point1, Point point2){
        return Math.abs(point1.getX()-point2.getX())+Math.abs(point1.getY()-point2.getY());
    }

    /**
     * return a distance from point 1 to point 3 passing point 2
     * @return
     */
    public static int calculate_distance(Point point1, Point point2,Point point3){
        return calculate_distance(point1,point2)+calculate_distance(point2,point3);
    }

    /**
     * get the index of the car that has the minimal distance towards customer
     * @param car_list
     * @param trip
     * @param size
     * @param a
     * @param set
     * @return
     */


    static int[] get_index(Car_list car_list, Trip trip, int size, double[] a, HashSet<Integer> set) {
        double time_remain;
        int[] index;
        for(int i = 0; i<size; i++){
            Car car=car_list.car_list.get(i);
            time_remain=car.getTime_stamp_arrive_des()-trip.getStartTime();
            if(time_remain>0) {
                a[i] = calculate_distance(car.getPosition(),trip.getSrc())+time_remain;
                set.add(i);
            }else{
                a[i]= calculate_distance(car.getPosition(),trip.getSrc());
            }
        }
        index=sort_array(a);
        return index;
    }

//    /**
//     * let the first car be the output
//     * @param car_list
//     * @param trip
//     * @param a
//     * @param index
//     * @param set
//     * @return
//     */
//
//    static int car_point_set_first(Car_list car_list, Trip trip, double[] a, int[] index, HashSet<Integer> set) {
//        //  System.out.println(Des.get_time()+" "+time_from_car_to_customer+"  "+time);
//        trip.setTimestamp_picked_up(a[0]);
//        trip.calculate_end_timestamp(car_list.car_list.get(index[0]));
//        car_list.car_list.get(index[0]).setTime_stamp_arrive_des(trip.getEnd_time());
//        car_list.car_list.get(index[0]).setPosition(trip.getDest());
//        if(set.contains(index[0])){
//            System.out.println("car "+ index[0]);
//        }
//        return index[0];
//    }
//
//    /**
//     *  let the ith car be the output
//     * @param car_list
//     * @param trip
//     * @param a
//     * @param index
//     * @param set
//     * @param i
//     * @return
//     */
//    static int car_point_set_i(Car_list car_list, Trip trip, double[] a, int[] index, HashSet<Integer> set, int i) {
//        trip.calculate_end_time(a[i],car_list.car_list.get(index[i]));
//        car_list.car_list.get(index[i]).setTime_stamp_arrive_des(trip.getEnd_time());
//        car_list.car_list.get(index[i]).setPosition(trip.getDest());
//        if(set.contains(index[i])){
//            System.out.println("car "+ index[0]);
//        }
//        return index[i];
//    }









}

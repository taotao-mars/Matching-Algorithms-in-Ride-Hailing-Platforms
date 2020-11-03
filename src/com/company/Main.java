package com.company;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.company.AdditionalFunctions.*;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int k = 0;//car
        //0-29 electronic car 30-99 normal car
        int n = 0;//customer
        int k_i;
        int max_waiting_time=0;
     //   writeTxtFile("output.csv",",Scenario,Vehicles,Passengers,Acceptance Rate,max_waiting time ,,,S(Rj) - S(Cj) - (N-M) * K - Customer Satisfaction Penalty,,,\n");
        writeTxtFile("output.csv","Scenarios,A1,A2,A3,A4,A5,A6");
        File file = new File("input.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            int line=1;
            while ((tempString = reader.readLine()) != null) {
                String data[]=tempString.split(" ");
                k=Integer.parseInt(data[0]);
                n=Integer.parseInt(data[1]);
                max_waiting_time=Integer.parseInt(data[2]);
                k_i=Integer.parseInt(data[3]);
                Test test = new Test(k, n).invoke();
                Write_content("output.csv","\nS"+line);
                GreedySchedulingAlgorithmPlusWaitingTime_version1.simulate(test.getCar_list0(),test.getDemand0(),n,max_waiting_time,k_i);
                GreedySchedulingAlgorithmPlusWaitingTime_version2.simulate(test.getCar_list1(),test.getDemand1(),n,max_waiting_time,k_i);
                RideSharingAlgorithmBasedOnVersion2.simulate(test.getCar_list2(),test.getDemand2(),n,max_waiting_time,k_i);
                GreedySchedulingAlgorithmPlusWaitingTime_version1_Elec.simulate(test.getCar_list3(),test.getDemand3(),n,max_waiting_time,k_i);
                GreedySchedulingAlgorithmPlusWaitingTime_version2_Elec.simulate(test.getCar_list4(),test.getDemand4(),n,max_waiting_time,k_i);
                RideSharingAlgorithmBasedOnVersion2_Elec.simulate(test.getCar_list5(),test.getDemand5(),n,max_waiting_time,k_i);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

    }

    private static class Test {
        private int k;
        private int n;
        private Customer_demand demand0;
        private Customer_demand demand1;
        private Customer_demand demand2;
        private Customer_demand demand3;
        private Customer_demand demand4;
        private Customer_demand demand5;
        private Car_list car_list0;
        private Car_list car_list1;
        private Car_list car_list2;
        private Car_list car_list3;
        private Car_list car_list4;
        private Car_list car_list5;

        public Test(int k, int n) {
            this.k = k;
            this.n = n;
        }

        public Customer_demand getDemand0() {
            return demand0;
        }

        public Customer_demand getDemand1() {
            return demand1;
        }

        public Customer_demand getDemand2() {
            return demand2;
        }

        public Customer_demand getDemand3() {
            return demand3;
        }

        public Customer_demand getDemand4() {
            return demand4;
        }
        public Customer_demand getDemand5() {
            return demand5;
        }

        public Car_list getCar_list0() {
            return car_list0;
        }

        public Car_list getCar_list1() {
            return car_list1;
        }

        public Car_list getCar_list2() {
            return car_list2;
        }

        public Car_list getCar_list3() {
            return car_list3;
        }

        public Car_list getCar_list4() {
            return car_list4;
        }
        public Car_list getCar_list5() {
            return car_list5;
        }

        public Test invoke() throws IOException, ClassNotFoundException {
            //Create n random customers and their destination
            demand0 = new Customer_demand();
            demand1 = new Customer_demand();
            demand2 = new Customer_demand();
            demand3 = new Customer_demand();
            demand4 = new Customer_demand();
            demand5 = new Customer_demand();
            List<Integer> time_list=randomList(1,10*3600,n);
            for (int j = 0; j < n; j++) {
                Point p = new Point(random_num(), random_num());
                Point p1 = new Point(random_num(), random_num());
                Trip t=new Trip(p,p1,time_list.get(j),j);
                demand0.trip_list.add(t);
            }
            demand1.trip_list=deepCopy(demand0.trip_list);
            demand2.trip_list=deepCopy(demand0.trip_list);
            demand3.trip_list=deepCopy(demand0.trip_list);
            demand4.trip_list=deepCopy(demand0.trip_list);
            demand5.trip_list=deepCopy(demand0.trip_list);
            car_list0 = new Car_list();
            car_list1 = new Car_list();
            car_list2 = new Car_list();
            car_list3 = new Car_list();
            car_list4 = new Car_list();
            car_list5 = new Car_list();
            for(int i=0;i<k;i++){
                Point p = new Point(random_num(), random_num());
                Car car=new Car(p);
                car_list0.car_list.add(car);
            }
            car_list1.car_list=deepCopy(car_list0.car_list);
            car_list2.car_list=deepCopy(car_list0.car_list);
            car_list3.car_list=deepCopy(car_list0.car_list);
            for(int i=0;i<k*0.3;i++){
                car_list3.car_list.get(i).setIs_electronic(true);
            }
            car_list4.car_list=deepCopy(car_list3.car_list);
            car_list5.car_list=deepCopy(car_list3.car_list);

            return this;
        }
    }
}

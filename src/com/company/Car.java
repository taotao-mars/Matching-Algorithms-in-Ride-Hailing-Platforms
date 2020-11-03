package com.company;

import java.io.Serializable;

public class Car implements Serializable {
    public Point position;
    public double time_stamp_arrive_des;
    public double speed;
    public boolean is_electronic;
    public int Distance_traveled;
    Car(){

    }
    Car(Point position){
        this.position=position;
        time_stamp_arrive_des=0;
        speed=1.0;
        is_electronic=false;
        Distance_traveled=0;
    }

    public void setIs_electronic(boolean is_electronic) {
        this.is_electronic = is_electronic;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getTime_stamp_arrive_des() {
        return time_stamp_arrive_des;
    }

    public void setTime_stamp_arrive_des(double time_stamp_arrive_des) {
        this.time_stamp_arrive_des = time_stamp_arrive_des;
    }
}

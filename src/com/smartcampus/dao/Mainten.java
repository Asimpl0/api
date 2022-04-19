package com.smartcampus.dao;

import java.util.Date;

public class Mainten {
    public int mid;
    public Date bdate;
    public String edate;
    public String place;
    public String detail;
    public String status;

    public void setMid(int mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "Mainten{" +
                "mid=" + mid +
                ", bdate=" + bdate +
                ", edate=" + edate +
                ", place='" + place + '\'' +
                ", detail='" + detail + '\'' +
                ", status=" + status +
                '}';
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public void setEdate(Date edate) {
        if (edate == null)
            this.edate = "待处理";
        else
            this.edate = String.valueOf(bdate);
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStatus(int status) {
        if (status == 0)
            this.status = "未完成";
        else
            this.status = "已完成";
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


}

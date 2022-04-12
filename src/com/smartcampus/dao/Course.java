package com.smartcampus.dao;

public class Course {
    public String cname;
    public String tname;
    public String room;
    public  int weeks;
    public int wbegin;
    public int nums;
    public int[] tdetail;

    public void setTdetail(String[] tdetail) {
        this.tdetail = new int[tdetail.length];
        for (int i = 0; i < tdetail.length; i++) {
            this.tdetail[i] = Integer.valueOf(tdetail[i]);
        }
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public void setWbegin(int wbegin) {
        this.wbegin = wbegin;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }


}

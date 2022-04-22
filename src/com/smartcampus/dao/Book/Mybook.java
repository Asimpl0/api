package com.smartcampus.dao.Book;

public class Mybook {
    public int boid;
    public String bname;
    public String bsdate;
    public String bedate;
    public String brate;

    public void setBoid(int boid) {
        this.boid = boid;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setBsdate(String bsdate) {
        this.bsdate = bsdate;
    }

    public void setBedate(String bedate) {
        this.bedate = "null".equals(bedate)?"待还":bedate;
    }

    public void setBrate(String brate) {
        this.brate = "-1".equals(brate)?"未评价":brate;
    }
}

package com.smartcampus.dao.Book;

public class BookInfo {
    public int bid;
    public String bname;
    public int bnum;
    public String bauthor;
    public String bpublisher;
    public String btype;

    @Override
    public String toString() {
        return "BookInfo{" +
                "bid=" + bid +
                ", bname='" + bname + '\'' +
                ", bnum=" + bnum +
                ", bauthor='" + bauthor + '\'' +
                ", bpublisher='" + bpublisher + '\'' +
                ", btype='" + btype + '\'' +
                '}';
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }

    public void setBauthor(String bauthor) {
        this.bauthor = bauthor;
    }

    public void setBpublisher(String bpublisher) {
        this.bpublisher = bpublisher;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }
}

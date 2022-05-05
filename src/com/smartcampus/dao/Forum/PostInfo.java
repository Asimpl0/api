package com.smartcampus.dao.Forum;

public class PostInfo {
    public int pid;
    public String nickName;
    public String avatarUrl;
    public String ptime;

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int ptag;
    public String pdetail;
    public int cnums;
    public int lnums;
    public int conums;

    public void setIsmark(int ismark) {
        this.ismark = ismark;
    }

    public void setMark(int mark) {
        this.mark = String.valueOf(mark/2.0);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int ismark;
    public String mark;
    public String name;
    public void setPdetail(String pdetail) {
        this.pdetail = pdetail;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public void setPtag(int ptag) {
        this.ptag = ptag;
    }

    public void setCnums(int cnums) {
        this.cnums = cnums;
    }

    public void setInums(int lnums) {
        this.lnums = lnums;
    }

    public void setConums(int conums) {
        this.conums = conums;
    }
}

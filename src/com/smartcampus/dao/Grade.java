package com.smartcampus.dao;

public class Grade{
    public String Cname;//课程名
    public String Type;//课程类型
    public float credit;//学分
    public String grade;//成绩

    public Grade() {
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
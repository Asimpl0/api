package com.smartcampus.dao;

public class Grade{
    public String cname;//课程名
    public String type;//课程类型
    public float credit;//学分
    public String grade;//成绩

    public Grade() {
    }

    @Override
    public String toString() {
        return "Grade{" +
                "cname='" + cname + '\'' +
                ", type='" + type + '\'' +
                ", credit=" + credit +
                ", grade='" + grade + '\'' +
                '}';
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
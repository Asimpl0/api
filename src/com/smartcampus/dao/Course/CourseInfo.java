package com.smartcampus.dao.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseInfo {
    public String cid;
    public String cname;
    public String type;
    public String credit;
    public List<TeacherInfo> teachers;

    public CourseInfo() {
        this.teachers = new ArrayList<>();
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}


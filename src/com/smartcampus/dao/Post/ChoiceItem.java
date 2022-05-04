package com.smartcampus.dao.Post;

import java.util.List;

class ChildrenItem{
    public String text;
    public int id;

    public ChildrenItem() {
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }
}

public class ChoiceItem {
    public String text;
    public List<ChildrenItem> children;

    public ChoiceItem() {
    }

    public ChoiceItem(String text, List<ChildrenItem> children) {
        this.text = text;
        this.children = children;
    }
}

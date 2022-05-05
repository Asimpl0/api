package com.smartcampus.dao.Posting;

public class CommentInfo {
    public String cid;
    public String nickName;
    public String avatarUrl;
    public String ctime;
    public String cdetail;

    public void setPid(String cid) {
        this.cid = cid;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public void setCdetail(String cdetail) {
        this.cdetail = cdetail;
    }
}

package com.bigwis.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */
public class Tiangou {

    private boolean status;
    private int total;
    private List<TngouBean> tngou;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TngouBean> getTngou() {
        return tngou;
    }

    public void setTngou(List<TngouBean> tngou) {
        this.tngou = tngou;
    }

    public static class TngouBean {
        private int count;
        private String description;
        private int fcount;
        private String fromname;
        private String fromurl;
        private int id;
        private String img;
        private String keywords;
        private int rcount;
        private long time;
        private String title;
        private int topclass;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getFcount() {
            return fcount;
        }

        public void setFcount(int fcount) {
            this.fcount = fcount;
        }

        public String getFromname() {
            return fromname;
        }

        public void setFromname(String fromname) {
            this.fromname = fromname;
        }

        public String getFromurl() {
            return fromurl;
        }

        public void setFromurl(String fromurl) {
            this.fromurl = fromurl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public int getRcount() {
            return rcount;
        }

        public void setRcount(int rcount) {
            this.rcount = rcount;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTopclass() {
            return topclass;
        }

        public void setTopclass(int topclass) {
            this.topclass = topclass;
        }
    }
}

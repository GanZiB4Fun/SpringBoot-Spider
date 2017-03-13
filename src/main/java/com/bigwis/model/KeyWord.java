package com.bigwis.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/3/8.
 */
public class KeyWord {
    public String keyword;

    public String keywordUrl;

    public String content;

    public String source;

    public Date dateTime;

    public KeyWord(String keyword){
        this.setKeyword(keyword);
    }

    public KeyWord() {
        this.keyword = keyword;
        this.keywordUrl = keywordUrl;
        this.content = content;
        this.source = source;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeywordUrl() {
        return keywordUrl;
    }

    public void setKeywordUrl(String keywordUrl) {
        this.keywordUrl = keywordUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "KeyWord{" +
                "keyword='" + keyword + '\'' +
                ", keywordUrl='" + keywordUrl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

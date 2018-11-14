package com.noob.storage;

import java.util.Date;

/**
 * @author luyun
 * @since 2018.10.08 10:52
 */
public class HomeEntranceCfg {

    private String title;

    private String imageUrl;

    private String contentUrl;

    /**
     * 如果当前时间小于 startTime 则不显示
     */
    private Date startTime;

    /**
     * 如果当前时间大于 endTime 则不显示
     */
    private Date endTime;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

package com.daylyweb.music.mapper;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FeedBack {
    private Integer id;

    private String name;

    private String concat;

    private String device;

    private String content;

    private Date time;

    public FeedBack() {}
    
    public FeedBack(String name,String concat,String device,String content,Date time) {
    	this.setName(name);
    	this.setConcat(concat);
    	this.setDevice(device);
    	this.setContent(content);
    	this.setTime(time);
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getConcat() {
        return concat;
    }

    public void setConcat(String concat) {
        this.concat = concat == null ? null : concat.trim();
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device == null ? null : device.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
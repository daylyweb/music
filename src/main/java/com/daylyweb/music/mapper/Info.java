package com.daylyweb.music.mapper;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Info {
    private Integer id;

    private String info;

    private Date time;

    public Info() {
		super();
	}

	public Info(Integer id, String info, Date time) {
		super();
		this.id = id;
		this.info = info;
		this.time = time;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Date getTime() {
        return time;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
    public void setTime(Date time) {
        this.time = time;
    }
}
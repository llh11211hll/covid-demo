package com.example.getInfo;

import lombok.Data;

@Data//提供基本的get，set方法
public class Header
{
    private String origin;
    private String referer;
    private String userAgent;

    public Header(String origin,String referer,String userAgent)
    {
        this.origin=origin;
        this.referer=referer;
        this.userAgent=userAgent;
    }

    @Override
    public String toString()
    {
        return "origin="+origin+"&"+"referer"+referer+"&"+"user-agent="+userAgent;
    }
}

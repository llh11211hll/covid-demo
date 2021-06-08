package com.example.service;

import com.example.dao.COVIDinfo;

import java.util.List;

public interface CovidInfoService
{
    public List<COVIDinfo> getCovidInfoList();
    public COVIDinfo findCovidInfoById(long id);
}

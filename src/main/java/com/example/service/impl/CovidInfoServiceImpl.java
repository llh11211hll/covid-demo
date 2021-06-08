package com.example.service.impl;

import com.example.dao.COVIDinfo;
import com.example.repository.CovidInfoRepository;
import com.example.service.CovidInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovidInfoServiceImpl implements CovidInfoService
{
    @Autowired
    private CovidInfoRepository covidInfoRepository;

    @Override
    public List<COVIDinfo> getCovidInfoList()
    {
        return covidInfoRepository.findAll();
    }

    @Override
    public COVIDinfo findCovidInfoById(long id)
    {
        return covidInfoRepository.findById(id);
    }
}

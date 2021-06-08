package com.example.controller;

import com.example.dao.COVIDinfo;
import com.example.repository.CovidInfoRepository;
import com.example.tasks.GetInfoTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController
{
    @Autowired
    private CovidInfoRepository covidInfoRepository;

    @Autowired
    JdbcTemplate jdbcTemplate1;

    @GetMapping("/")
    public ModelAndView showIndex()
    {
        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
        String timeNow=simpleDateFormat.format(date1).substring(0,10);

        final String sql="SELECT * FROM Covid_"+timeNow;
        System.out.println(sql);
        //List<COVIDinfo> list = jdbcTemplate1.query(sql,new BeanPropertyRowMapper<>(COVIDinfo.class));//由于前面的是周期性执行，这里的jdbcTemplate又不能用了，只好写一个新的
        BeanPropertyRowMapper<COVIDinfo> mapper = new BeanPropertyRowMapper<>(COVIDinfo.class);
        List<COVIDinfo> query = jdbcTemplate1.query(sql,mapper);
        for(COVIDinfo covid:query) {
            System.out.println(covid);
        }
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("covid",query);

        return mav;
    }

}

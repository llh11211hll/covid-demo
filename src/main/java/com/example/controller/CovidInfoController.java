package com.example.controller;

import com.example.dao.COVIDinfo;
import com.example.repository.CovidInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.CriteriaBuilder;

@Controller
@RequestMapping("CovidInfoEdit")
public class CovidInfoController
{
    @Autowired
    private CovidInfoRepository covidInfoRepository;

    @RequestMapping("")
    public ModelAndView covidInfoList(@RequestParam(value = "start", defaultValue = "0") Integer start,
                                      @RequestParam(value = "limit", defaultValue = "5") Integer limit)//文章列表，使用了分页
    {
        start = start<0?0:start;
        Sort sort=Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start,limit,sort);
        Page<COVIDinfo> page = covidInfoRepository.findAll(pageable);
        ModelAndView mav = new ModelAndView("covid/list");
        mav.addObject("page",page);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getCovidInfo(@PathVariable("id") Integer id)
    {
        COVIDinfo covidinfos = covidInfoRepository.findById(id);
        ModelAndView mav = new ModelAndView("covid/show");
        mav.addObject("covidinfo",covidinfos);
        return mav;
    }

}

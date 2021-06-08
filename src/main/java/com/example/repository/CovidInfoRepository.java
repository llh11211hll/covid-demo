package com.example.repository;

import com.example.dao.COVIDinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CovidInfoRepository extends JpaRepository<COVIDinfo,Long>, JpaSpecificationExecutor<COVIDinfo>
{
    COVIDinfo findById(long id);//
    List<COVIDinfo> findAll();

}

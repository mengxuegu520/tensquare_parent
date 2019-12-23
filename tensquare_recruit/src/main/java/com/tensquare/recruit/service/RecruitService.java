package com.tensquare.recruit.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.recruit.dao.RecruitDao;
import com.tensquare.recruit.pojo.Recruit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class RecruitService {

	@Autowired
	private RecruitDao recruitDao;
	
	@Autowired
	private IdWorker idWorker;


	public  List<Recruit> findByStateOrderByCreatetimeDesc(){
		return  recruitDao.findByStateOrderByCreatetimeDesc("2");
	}
	public  List<Recruit> findByStateNotOrderByCreatetimeDesc(){
		return  recruitDao.findByStateNotOrderByCreatetimeDesc("0");
	}
}

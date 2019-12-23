package com.tensquare.recruit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.recruit.pojo.Recruit;
import com.tensquare.recruit.service.RecruitService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @RequestMapping(value = "/search/recommend", method = RequestMethod.GET)
    public Result recommend() {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findByStateOrderByCreatetimeDesc());
    }
    @RequestMapping(value = "/search/newlist", method = RequestMethod.GET)
    public Result newlist() {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findByStateNotOrderByCreatetimeDesc());
    }
}

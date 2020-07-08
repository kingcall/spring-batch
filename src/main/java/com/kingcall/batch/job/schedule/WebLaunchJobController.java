package com.kingcall.batch.job.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebLaunchJobController {
    @Autowired
    WebJobLaunchService launch;

    @Autowired
    WebJobLaunchService2 operator;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @ResponseBody
    public String home() {
        return "你好，Spring Boot";
    }

    @RequestMapping(value = "/job/{jobName}", method = RequestMethod.GET)
    @ResponseBody
    public String runJob(@PathVariable("jobName") String jobName) {
        System.out.println( "你好，Spring Boot batch job "+ jobName);
        launch.runJob(jobName);
        return "你好，Spring Boot  batch job "+ jobName;
    }

    @RequestMapping(value = "/job2/{jobName}", method = RequestMethod.GET)
    @ResponseBody
    public String runJob2(@PathVariable("jobName") String jobName) {
        System.out.println( "你好，Spring Boot batch job "+ jobName);
        operator.runJob(jobName);
        return "你好，Spring Boot  batch job "+ jobName;
    }


    public static void main(String[] args) {
        SpringApplication.run(WebLaunchJobController.class, args);
    }
}

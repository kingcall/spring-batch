package com.kingcall.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

import static java.lang.String.format;

/**
 * 可以使用XNL 的方式运行job
 */
public class JobLaunch {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "batch-demo.xml");
        // 将 jobLauncher 作为参数传进来，即可启动自定义的某个job
        String jobName = args[0];
        JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("helloWorldJob");

        try {
            /* 运行Job */
            JobExecution result = launcher.run(job, new JobParameters());
            /* 处理结束，控制台打印处理结果 */
            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main2(String[] args) {
        String jobName = args[0];

        try {
            ConfigurableApplicationContext context = SpringApplication.run(JobLaunch.class, args);
            JobRegistry jobRegistry = context.getBean(JobRegistry.class);
            Job job = jobRegistry.getJob(jobName);
            JobLauncher jobLauncher = context.getBean(JobLauncher.class);
            JobExecution jobExecution = jobLauncher.run(job, createJobParams());
            if (!jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                throw new RuntimeException(format("%s Job execution failed.", jobName));
            }
        } catch (Exception e) {
            throw new RuntimeException(format("%s Job execution failed.", jobName));
        }
    }

    private static JobParameters createJobParams() {
        return new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
    }
}

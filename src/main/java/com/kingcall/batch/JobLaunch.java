package com.kingcall.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 可以使用XNL 的方式运行job
 */
public class JobLaunch {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "batch-demo.xml");
        // 将 jobLauncher 作为参数传进来，即可启动自定义的某个job
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
}

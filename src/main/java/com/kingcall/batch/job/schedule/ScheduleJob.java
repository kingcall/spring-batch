package com.kingcall.batch.job.schedule;

import com.kingcall.batch.job.listener.SkipListenerJob;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//@Configuration
//@EnableScheduling
public class ScheduleJob {
    long i = 1;
    @Autowired
    JobLauncher launcher;

    @Autowired
    ApplicationContext context;

    @Scheduled(fixedDelay = 1000)
    public void cchedule() {
        // 需要注意的是你这里必须要有一个名为这个的job
        runJob("skipJob");
    }

    public void runJob(String jobName) {
        System.out.println("开始运行");
        Job job = (Job) context.getBean(jobName);
        try {
            launcher.run(job, createJobParams());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    private JobParameters createJobParams() {
        return new JobParametersBuilder().addLong("date", i++).toJobParameters();
    }

}

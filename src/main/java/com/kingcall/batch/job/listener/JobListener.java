package com.kingcall.batch.job.listener;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableBatchProcessing
public class JobListener implements JobExecutionListener {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job jobListenerGetJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(jobListenerStep1())
                .build()
                ;
    }

    @Bean
    public Step jobListenerStep1() {
        return stepBuilders
                .get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("hello spring batch JobListener 1");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("============================== beforeJob ===================================");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("============================== afterJob ===================================");

    }
}

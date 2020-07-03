package com.kingcall.batch.job.simpeJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableBatchProcessing
public class HelloWorldSpringBtachSingleStepJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job single() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(singleStep1())
                .build()
                ;
    }

    @Bean
    public Step singleStep1() {
        return stepBuilders
                .get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("hello spring batch 1");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }

}

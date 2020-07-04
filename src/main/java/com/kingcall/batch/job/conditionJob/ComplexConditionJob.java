package com.kingcall.batch.job.conditionJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
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
public class ComplexConditionJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job flowJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(step1())
                .on("COMPLETED").to(step2())
                .on("COMPLETED").to(step3())
                .end()
                .build()
                ;
    }

    @Bean
    public Step step1() {
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

    @Bean
    public Step step2() {
        return stepBuilders
                .get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("hello spring batch 2");
                        int x=1 / 0;
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }

    @Bean
    public Step step3() {
        return stepBuilders
                .get("step3")
                .tasklet((stepContribution, chunkContext)->{
                    System.out.println("hello spring batch 3");
                    return RepeatStatus.FINISHED;
                })
                .build()
                ;
    }
}

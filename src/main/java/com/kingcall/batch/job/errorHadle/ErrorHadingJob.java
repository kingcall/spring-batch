package com.kingcall.batch.job.errorHadle;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

//@Configuration
//@EnableBatchProcessing
public class ErrorHadingJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;


    @Bean
    public Job errorJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(step1())
                .next(step2())
                .build()
                ;
    }

    @Bean
    public Step step1() {
        return stepBuilders
                .get("step1")
                .tasklet(task1())
                .build()
                ;
    }

    @Bean
    public Step step2() {
        return stepBuilders
                .get("step2")
                .tasklet(task1())
                .build()
                ;
    }

    @Bean
    @StepScope
    public Tasklet task1() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                Map<String, Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
                if (stepExecutionContext.containsKey("first")) {
                    System.out.println("second will sucess ..... ");
                    return RepeatStatus.FINISHED;
                } else {
                    System.out.println("first will fail ....");
                    chunkContext.getStepContext().getStepExecutionContext().put("first", true);
                    throw new RuntimeException("Errors occurs");
                }
            }
        };
    }

}

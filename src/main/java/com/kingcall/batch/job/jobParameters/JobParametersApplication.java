package com.kingcall.batch.job.jobParameters;


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

import java.util.Map;

/**
 * info=kingcall 传递给程序
 */

//@Configuration
//@EnableBatchProcessing
public class JobParametersApplication implements StepExecutionListener {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;
    Map<String, JobParameter> parameters;

    @Bean
    public Job parametersJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(parametersJobStep1())
                .build()
                ;
    }

    @Bean
    public Step parametersJobStep1() {
        return stepBuilders
                .get("step1")
                .listener(this)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(String.format("hello spring batch parameters %s", parameters.get("info")));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        parameters = stepExecution.getJobParameters().getParameters();

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}

package com.kingcall.batch.job.simpeJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.job.flow.Flow;

//@Configuration
//@EnableBatchProcessing
public class FlowJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job btachFlow() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                // start() 是有两个重载的方法
                .start(jobFlow1())
                .next(btachFlowStep3())
                .end()
                .build();
    }

    @Bean
    public Step btachFlowStep1() {
        return stepBuilders
                .get(this.getClass().getSimpleName()+" ----- step1")
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
    public Step btachFlowStep2() {
        return stepBuilders
                .get(this.getClass().getSimpleName()+" ----- step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("hello spring batch 2");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }
    @Bean
    public Step btachFlowStep3() {
        return stepBuilders
                .get(this.getClass().getSimpleName()+" ----- step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("hello spring batch 3");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }


    @Bean
    public Flow jobFlow1(){
        return new FlowBuilder<Flow>("flow1")
                .start(btachFlowStep1())
                .next(btachFlowStep2())
                .build()
                ;
    }


}

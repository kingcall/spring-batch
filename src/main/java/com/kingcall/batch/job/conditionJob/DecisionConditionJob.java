package com.kingcall.batch.job.conditionJob;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  可以支持通配符
 */
//@Configuration
//@EnableBatchProcessing
public class DecisionConditionJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job decisionJob() {
        return jobBuilderFactory.get(this.getClass().getSimpleName())
                .start(decisionConditionJobStep1())
                .next(decider())
                .from(decider()).on("EVEN").to(decisionConditionJobEvenStep())
                .from(decider()).on("ODD").to(decisionConditionJobOddStep())
                .next(decisionConditionJobEndStep())
                .end()
                .build()
                ;
    }

    @Bean
    public Job decisionJob2() {
        return jobBuilderFactory.get(this.getClass().getSimpleName())
                .start(decisionConditionJobStep1())
                .next(decider())
                    .from(decider()).on("EVEN").to(decisionConditionJobEvenStep())
                    .from(decider()).on("ODD").to(decisionConditionJobOddStep())
                .from(decisionConditionJobEndStep()).on("*").to(decider())
                .end()
                .build()
                ;
    }

    @Bean
    public Step decisionConditionJobStep1() {
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
    public Step decisionConditionJobEvenStep() {
        return stepBuilders
                .get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("hello spring batch even");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }

    @Bean
    public Step decisionConditionJobOddStep() {
        return stepBuilders
                .get("step3")
                .tasklet((stepContribution, chunkContext)->{
                    System.out.println("hello spring batch odd");
                    return RepeatStatus.FINISHED;
                })
                .build()
                ;
    }

    @Bean
    public Step decisionConditionJobEndStep() {
        return stepBuilders
                .get("step4")
                .tasklet((stepContribution, chunkContext)->{
                    System.out.println("hello spring batch end");
                    return RepeatStatus.FINISHED;
                })
                .build()
                ;
    }



    @Bean
    public JobExecutionDecider decider(){
        return new JobExecutionDecider() {
            private int count = 0;
            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                count++;
                if (count % 2 ==0){
                    return new FlowExecutionStatus("EVEN");
                }else {
                    return new FlowExecutionStatus("ODD");
                }

            }
        };
    }
}

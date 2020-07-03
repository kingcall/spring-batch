package com.kingcall.batch.job.splitJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * 多线程执行(可以看出batch1 都是先执行的)
 *  SimpleAsyncTaskExecutor-1 hello spring batch 1
 *  SimpleAsyncTaskExecutor-2 hello spring batch 1
 *  SimpleAsyncTaskExecutor-1 hello spring batch 2
 *  SimpleAsyncTaskExecutor-2 hello spring batch 2
 */
//@Configuration
//@EnableBatchProcessing
public class HelloWorldSpringBtachSplitJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job single() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(splitJobFlow1())
                .split(new SimpleAsyncTaskExecutor())
                .add(splitJobFlow2())
                .end()
                .build()
                ;
    }

    @Bean
    public Step splitFlow1Step1() {
        return stepBuilders
                .get("splitFlow1Step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(String.format(" %s hello spring batch 1", Thread.currentThread().getName()));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }

    @Bean
    public Step splitFlow1Step2() {
        return stepBuilders
                .get("splitFlow1Step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(String.format(" %s hello spring batch 2", Thread.currentThread().getName()));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }
    @Bean
    public Step splitFlow2Step1() {
        return stepBuilders
                .get("splitFlow2Step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(String.format(" %s hello spring batch 1", Thread.currentThread().getName()));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }

    @Bean
    public Step splitFlow2Step2() {
        return stepBuilders
                .get("splitFlow2Step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(String.format(" %s hello spring batch 2", Thread.currentThread().getName()));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }


    @Bean
    public Flow splitJobFlow1(){
        return new FlowBuilder<Flow>("flow1")
                .start(splitFlow1Step1())
                .next(splitFlow1Step2())
                .build()
                ;
    }

    @Bean
    public Flow splitJobFlow2(){
        return new FlowBuilder<Flow>("flow2")
                .start(splitFlow2Step1())
                .next(splitFlow2Step2())
                .build()
                ;
    }

}

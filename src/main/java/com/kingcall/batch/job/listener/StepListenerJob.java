package com.kingcall.batch.job.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

//@Configuration
//@EnableBatchProcessing
public class StepListenerJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job stepJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(stepListenerJobStep1())
                .build()
                ;
    }

    @Bean
    public Step stepListenerJobStep1() {
        return stepBuilders
                .get("stepListenerJobStep1")
                .chunk(2)
                .faultTolerant()
                .listener(new MyStepistener())
                .reader(reader())
                .writer(writer())
                .build()
                ;
    }

    @Bean
    public Step stepListenerJobStep2() {
        return stepBuilders
                .get("stepListenerJobStep2")
                .listener(new MyStepistener())
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("................................... step  processing ...................");
                    return RepeatStatus.FINISHED;
                }))
                .build()
                ;
    }


    @Bean
    public ItemWriter writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                list.forEach(line-> System.out.println(line));
            }
        };
    }

    @Bean
    public ItemReader<String> reader() {
        return new ListItemReader<>(Arrays.asList("a", "b", "c"));
    }


    class MyStepistener{
        @BeforeStep
        public void beforeStep(ChunkContext chunkContext) {
            System.out.println(String.format("============================== %s before step ===================================", chunkContext.getStepContext().getStepName()));
        }

        @AfterStep
        public void afterStep(ChunkContext chunkContext) {
            System.out.println(String.format("============================== %s after step ===================================", chunkContext.getStepContext().getStepName()));
        }
    }

}







package com.kingcall.batch.job.listener;

import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;


import java.util.Arrays;
import java.util.List;

//@Configuration
//@EnableBatchProcessing
public class JobListener2 {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job jobListener2getJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(jobListener2Step1())
                .listener(new MyJobListener())
                .build()
                ;
    }

    @Bean
    public Step jobListener2Step1() {
        return stepBuilders
                .get("step1")
                .chunk(2)
                .faultTolerant()
                .listener(new MyChunkListener())
                .reader(reader())
                .writer(writer())
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


}

class MyJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(String.format("============================== %s beforeJob ===================================", jobExecution.getJobInstance().getJobName()));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(String.format("============================== %s afterJob ===================================", jobExecution.getJobInstance().getJobName()));
    }
}


class MyChunkListener {
    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        System.out.println(String.format("============================== %s beforeChunk ===================================", chunkContext.getStepContext().getStepName()));
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println(String.format("============================== %s afterChunk ===================================", chunkContext.getStepContext().getStepName()));
    }
}

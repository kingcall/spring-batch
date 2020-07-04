package com.kingcall.batch.job.complexJob;

import com.kingcall.batch.tasklets.ComplexJobTasklet1;
import com.kingcall.batch.tasklets.ComplexJobTasklet2;
import com.kingcall.batch.tasklets.ComplexJobTasklet3;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 前面所有job的代码都是在一个文件里的，其实每个step 应该有自己的代码
 */

//@Configuration
public class HelloWorldSpringBtachNestedChild2Job {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job nestedChild2Job() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(childJob2Step1())
                .next(childJob2Step2())
                .next(childJob2Step3())
                .build();
    }


    @Bean
    public Step childJob2Step1() {
        return stepBuilders
                .get(this.getClass().getSimpleName() + " ----- childJob2Step1")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("childJob2Step1");
                    return RepeatStatus.FINISHED;
                }))
                .build()
                ;
    }

    @Bean
    public Step childJob2Step2() {
        return stepBuilders
                .get(this.getClass().getSimpleName() + " ----- childJob2Step2")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("childJob2Step2");
                    return RepeatStatus.FINISHED;
                }))
                .build()
                ;
    }

    @Bean
    public Step childJob2Step3() {
        return stepBuilders
                .get(this.getClass().getSimpleName() + " ----- childJob2Step3")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("childJob2Step3");
                    return RepeatStatus.FINISHED;
                }))
                .build()
                ;
    }

}

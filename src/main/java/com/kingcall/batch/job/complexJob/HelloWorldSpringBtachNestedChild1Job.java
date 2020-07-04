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

@Configuration
public class HelloWorldSpringBtachNestedChild1Job {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job nestedChild1Job() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(childJob1Step1())
                .next(childJob1Step2())
                .next(childJob1Step3())
                .build();
    }


    @Bean
    public Step childJob1Step1() {
        return stepBuilders
                .get(this.getClass().getSimpleName() + " ----- childJob1Step1")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("childJob1Step1");
                    return RepeatStatus.FINISHED;
                }))
                .build()
                ;
    }

    @Bean
    public Step childJob1Step2() {
        return stepBuilders
                .get(this.getClass().getSimpleName() + " ----- childJob1Step2")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("childJob1Step2");
                    return RepeatStatus.FINISHED;
                }))
                .build()
                ;
    }

    @Bean
    public Step childJob1Step3() {
        return stepBuilders
                .get(this.getClass().getSimpleName() + " ----- childJob1Step3")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("childJob1Step3");
                    return RepeatStatus.FINISHED;
                }))
                .build()
                ;
    }


}

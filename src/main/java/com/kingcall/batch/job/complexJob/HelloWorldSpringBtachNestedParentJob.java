package com.kingcall.batch.job.complexJob;

import com.kingcall.batch.tasklets.ComplexJobTasklet1;
import com.kingcall.batch.tasklets.ComplexJobTasklet2;
import com.kingcall.batch.tasklets.ComplexJobTasklet3;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 前面所有job的代码都是在一个文件里的，其实每个step 应该有自己的代码
 */

@Configuration
@EnableBatchProcessing
public class HelloWorldSpringBtachNestedParentJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    JobLauncher launcher;


    @Autowired
    Job nestedChild1Job;
    @Autowired
    Job nestedChild2Job;


    @Bean
    public Job nestedJob(JobRepository repository, PlatformTransactionManager transactionManager) {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(childJob1(repository, transactionManager))
                .next(childJob2(repository, transactionManager))
                .build();
    }

    private Step childJob1(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJob1"))
                .job(nestedChild1Job)
                .launcher(launcher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build()
                ;
    }

    private Step childJob2(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(nestedChild2Job)
                .launcher(launcher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build()
                ;
    }




}

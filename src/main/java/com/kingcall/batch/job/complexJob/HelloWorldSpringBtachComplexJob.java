package com.kingcall.batch.job.complexJob;

import com.kingcall.batch.tasklets.ComplexJobTasklet1;
import com.kingcall.batch.tasklets.ComplexJobTasklet2;
import com.kingcall.batch.tasklets.ComplexJobTasklet3;
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


/**
 * 前面所有job的代码都是在一个文件里的，其实每个step 应该有自己的代码
 */

//@Configuration
//@EnableBatchProcessing
public class HelloWorldSpringBtachComplexJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    ComplexJobTasklet1 tasklet1;
    @Autowired
    ComplexJobTasklet2 tasklet2;
    @Autowired
    ComplexJobTasklet3 tasklet3;

    @Bean
    public Job btachFlow() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(btachFlowStep1())
                .next(btachFlowStep2())
                .next(btachFlowStep3())
                .build();
    }

    @Bean
    public Step btachFlowStep1() {
        return stepBuilders
                .get(this.getClass().getSimpleName()+" ----- step1")
                .tasklet(tasklet1)
                .build()
                ;
    }
    @Bean
    public Step btachFlowStep2() {
        return stepBuilders
                .get(this.getClass().getSimpleName()+" ----- step2")
                .tasklet(tasklet2)
                        .build()
                ;
    }
    @Bean
    public Step btachFlowStep3() {
        return stepBuilders
                .get(this.getClass().getSimpleName()+" ----- step3")
                .tasklet(tasklet3)
                .build()
                ;
    }



}

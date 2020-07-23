package com.kingcall.batch.job.simpeJob;

import com.kingcall.batch.utils.CommonConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//@Configuration
//@EnableBatchProcessing
public class ChunkJob {

    @Autowired
    JobLauncher launcher;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobExplorer jobExplorer;
    @Autowired
    JobRegistry jobRegistry;


    @Autowired
    ApplicationContext context;


    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    CommonConsoleItemWriter writer;

    @Bean
    public Job skipJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(step1())
                .build()
                ;
    }



    @Bean
    public Step step1() {
        return stepBuilders
                .get("step1")
                .<String, String>chunk(1)
                .reader(reader())
                .processor(process())
                .writer(writer)
                .faultTolerant()
                .skip(RuntimeException.class)// If a retry limit is provided then retryable exceptions must also be specified
                .skipLimit(3)
                .listener(this)
                .build()
                ;
    }

    public ItemProcessor process() {
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String s) throws Exception {
                System.out.println("processing item " + s);
                return s;
            }
        };
    }

    public ItemReader<String> reader() {
        List<String> data = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
        return new InputItemReader(data);

    }


    class InputItemReader implements ItemReader<String> {
        private final Iterator<String> iterator;

        public InputItemReader(List<String> datataList) {
            this.iterator = datataList.iterator();
        }

        @Override
        public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return null;
            }
        }
    }


}

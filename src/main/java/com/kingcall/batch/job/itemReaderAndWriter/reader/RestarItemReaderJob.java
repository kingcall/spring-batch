package com.kingcall.batch.job.itemReaderAndWriter.reader;

import com.kingcall.batch.models.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class RestarItemReaderJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("restarItemReader")
    private ItemStreamReader reader;

    @Autowired
    @Qualifier("commonConsoleItemWriter")
    private ItemWriter<? super Person> writer;

    @Bean
    public Job restartJob(){
        return jobBuilderFactory.get("restartJob")
                .start(restartStep())
                .build();

    }

    @Bean
    public Step restartStep() {
        return stepBuilderFactory.get("restartStep")
                .<Person,Person>chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
    }
}

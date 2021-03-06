package com.kingcall.batch.job.processorJob;

import com.alibaba.fastjson.JSON;
import com.kingcall.batch.models.Person;
import com.kingcall.batch.utils.CommonConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.function.Function;

//@Configuration
//@EnableBatchProcessing
public class SingleProcessorJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    CommonConsoleItemWriter writer;

    @Bean
    public Job processorJob() {
        return jobBuilderFactory.get("processorJob")
                .start(processorJobStep())
                .build();

    }


    @Bean
    public Step processorJobStep() {
        return stepBuilderFactory.get("processorJobStep")
                .<Person, Person>chunk(50)
                .reader(flatFileReader())
                .<Person,String>processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public ItemProcessor processor(){
        return new ItemProcessor< Person,String>() {
            @Override
            public String process(Person person) throws Exception {
                System.out.println("process ..... "+ person.getId());
                return JSON.toJSONString(person);
            }
        };
    }


    @Bean
    public FlatFileItemReader<Person> flatFileReader() {
        FlatFileItemReader<Person> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("csv/person.csv"));
        // 跳过表头
        flatFileItemReader.setLinesToSkip(1);
        // 分隔符
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id", "firstname", "lastname"});
        // line mapping
        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            return Person.builder()
                    .id(fieldSet.readInt("id"))
                    .firstName(fieldSet.readString("firstname"))
                    .lastName(fieldSet.readString("lastname"))
                    .build();
        });

        flatFileItemReader.setLineMapper(lineMapper);
        return flatFileItemReader;
    }

}

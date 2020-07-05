package com.kingcall.batch.job.itemReaderAndWriter.reader;

import com.kingcall.batch.models.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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

//@Configuration
//@EnableBatchProcessing
public class MultiResourceItemReaderJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("commonConsoleItemWriter")
    private ItemWriter<? super Person> writer;

    @Value("classpath*:/csv/person*.csv")
    private Resource[] inputFiles;

    @Bean
    public Job multipleFileDemoJob(){
        return jobBuilderFactory.get("multipleFileDemoJob")
                .start(multipleFileDemoStep())
                .build();

    }

    @Bean
    public Step multipleFileDemoStep() {
        return stepBuilderFactory.get("multipleFileDemoStep")
                .<Person,Person>chunk(50)
                .reader(multipleResourceItemReader())
                .writer(writer)
                .build();
    }

    private MultiResourceItemReader<Person> multipleResourceItemReader() {

        MultiResourceItemReader<Person> reader = new MultiResourceItemReader();

        reader.setDelegate(flatFileReader());
        reader.setResources(inputFiles);

        return reader;
    }

    @Bean
    public FlatFileItemReader<Person> flatFileReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("person.csv"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id","firstName","lastName"});

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper((fieldSet -> {
            return Person.builder().id(fieldSet.readInt("id"))
                    .firstName(fieldSet.readString("firstName"))
                    .lastName(fieldSet.readString("lastName"))
                    .build();
        }));
        lineMapper.afterPropertiesSet();
        reader.setLineMapper(lineMapper);

        return reader;

    }
}

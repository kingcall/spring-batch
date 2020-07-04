package com.kingcall.batch.job.itemReaderAndWriter.reader;

import com.kingcall.batch.models.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class XmlFileBatchItemReader {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;


    @Bean
    public Job jdbcItemReaderJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(jdbcItemReaderJobStep1())
                .build()
                ;
    }

    @Bean
    public Step jdbcItemReaderJobStep1() {
        return stepBuilders
                .get("itemReaderJobReadStep1")
                .<Person, Person>chunk(1)
                .reader(flatFileReader())
                .writer(list -> {
                    list.forEach(line -> System.out.println(String.format("current item is %s", line.toString())));
                })
                .build()
                ;
    }

    @Bean
    public FlatFileItemReader<Person> flatFileReader() {
        FlatFileItemReader<Person> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("csv/person.csv"));
        // 跳过表头
        flatFileItemReader.setLinesToSkip(1);
        // 分隔符
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[] {"id","firstname","lastname"});
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


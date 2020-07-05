package com.kingcall.batch.job.itemReaderAndWriter.writer;

import com.alibaba.fastjson.JSON;
import com.kingcall.batch.models.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class XmlFileBatchItemWriterJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;


    @Bean
    public Job xmlFileItemWriterJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(xmlFileBatchItemWriterJobtep())
                .build()
                ;
    }

    @Bean
    public Step xmlFileBatchItemWriterJobtep() {
        return stepBuilders
                .get("xmlFileBatchItemWriterJobtep")
                .<Person, Person>chunk(1)
                .reader(flatFileReader())
                .writer(xmlFileWriter())
                .build()
                ;
    }


    @Bean
    public StaxEventItemWriter<Person> xmlFileWriter() {
        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        Map<String, Class> mapClass = new HashMap<>();
        mapClass.put("person", Person.class);
        unMarshaller.setAliases(mapClass);
        StaxEventItemWriter<Person> writer = new StaxEventItemWriter<>();
        writer.setMarshaller(unMarshaller);
        writer.setRootTagName("persons");
        writer.setResource(new FileSystemResource("data/csv/person_write.xml"));
        return writer;
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


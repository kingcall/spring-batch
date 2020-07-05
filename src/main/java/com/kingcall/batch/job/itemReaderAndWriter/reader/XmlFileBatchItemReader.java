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
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
//@EnableBatchProcessing
public class XmlFileBatchItemReader {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;


    @Bean
    public Job XmlFileItemReaderJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(xmlFileItemReaderJobStep1())
                .build()
                ;
    }

    @Bean
    public Step xmlFileItemReaderJobStep1() {
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
    public StaxEventItemReader<Person> flatFileReader() {
        StaxEventItemReader<Person> reader = new StaxEventItemReader<>();

        reader.setResource(new ClassPathResource("csv/person.xml"));
        reader.setFragmentRootElementName("person");

        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        Map<String, Class> mapClass = new HashMap<>();

        mapClass.put("person", Person.class);

        unMarshaller.setAliases(mapClass);

        reader.setUnmarshaller(unMarshaller);
        return reader;
    }
}


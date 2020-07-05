package com.kingcall.batch.job.itemReaderAndWriter.writer;

import com.alibaba.fastjson.JSON;
import com.kingcall.batch.models.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class MultiResourceItemWriterJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job multipleFileWriteJob() {
        return jobBuilderFactory.get("multipleFileDemoJob")
                .start(classifyFileWriteJobStep())
                .build();

    }

    @Bean
    public Step multipleFileWriteJobStep() {
        return stepBuilderFactory.get("multipleFileDemoStep")
                .<Person, Person>chunk(50)
                .reader(flatFileReader())
                .writer(compositeItemWriter())
                .build();
    }

    @Bean
    public Step classifyFileWriteJobStep() {
        return stepBuilderFactory.get("multipleFileDemoStep")
                .<Person, Person>chunk(50)
                .reader(flatFileReader())
                .writer(classifierCompositeItemWriter())
                .stream(xmlFileWriter())
                .stream(jsonFileWriter())
                .build();
    }

    @Bean
    public CompositeItemWriter<Person> compositeItemWriter() {
        CompositeItemWriter<Person> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(xmlFileWriter(), jsonFileWriter()));
        return writer;
    }

    @Bean
    public ClassifierCompositeItemWriter<Person> classifierCompositeItemWriter() {
        ClassifierCompositeItemWriter<Person> writer = new ClassifierCompositeItemWriter<>();
        writer.setClassifier(new Classifier<Person, ItemWriter<? super Person>>() {
            @Override
            public ItemWriter<? super Person> classify(Person person) {

                if (person.getId() % 2 == 0) {
                    return xmlFileWriter();
                } else {
                    return jsonFileWriter();
                }
            }
        });
        return writer;
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
        writer.setResource(new FileSystemResource("data/csv/person_classify_write.xml"));
        return writer;
    }

    @Bean
    public FlatFileItemWriter<Person> jsonFileWriter() {
        FlatFileItemWriter<Person> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("data/csv/person_classify_write.json"));
        // 如何将对象转化为一行
        writer.setLineAggregator(new LineAggregator<Person>() {

            @Override
            public String aggregate(Person person) {
                return JSON.toJSONString(person);
            }
        });

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

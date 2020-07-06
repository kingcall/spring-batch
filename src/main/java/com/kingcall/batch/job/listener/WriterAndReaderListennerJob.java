package com.kingcall.batch.job.listener;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
//@Configuration
//@EnableBatchProcessing
public class WriterAndReaderListennerJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job itemJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(itemJobStep())
                .build()
                ;
    }

    @Bean
    public Step itemJobStep() {
        return stepBuilders
                .get("stepListenerJobStep1")
                .chunk(1)
                .faultTolerant()
                .reader(reader())
                .listener(new MessageItemReadListener())
                .writer(writer())
                .listener((ItemWriteListener) new MessageItemReadListener())
                .build()
                ;
    }

    @Bean
    public ItemWriter writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                list.forEach(line-> System.out.println(line));
            }
        };
    }

    @Bean
    public ItemReader<String> reader() {
        return new ListItemReader<>(Arrays.asList("a", "b", "c"));
    }

    class MessageItemReadListener implements ItemReadListener<String>, ItemWriteListener<String> {


        @Override
        public void beforeRead() {
            System.out.println("read before");
        }

        @Override
        public void afterRead(String s) {
            System.out.println(format(" read after %s",s));
        }

        @Override
        public void onReadError(Exception e) {
            System.err.println(format("%s%n", e.getMessage()));
        }

        @Override
        public void beforeWrite(List<? extends String> list) {
            System.out.println("write before");
        }

        @Override
        public void afterWrite(List<? extends String> list) {
            System.out.println("write after");
        }

        @Override
        public void onWriteError(Exception e, List<? extends String> list) {
            System.err.println(format("%s%n", e.getMessage()));
        }
    }

}

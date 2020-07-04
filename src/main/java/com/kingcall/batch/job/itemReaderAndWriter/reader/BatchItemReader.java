package com.kingcall.batch.job.itemReaderAndWriter.reader;
import java.util.Arrays;
import java.util.Iterator;
import	java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchItemReader {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job itemReaderJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(readerStep1())
                .build()
                ;
    }

    @Bean
    public Step readerStep1() {
        return stepBuilders
                .get("itemReaderJobReadStep1")
                .<String,String>chunk(2)
                .reader(reader())
                .writer(list -> {
                    list.forEach(line-> System.out.println(String.format("current item is %s",line )));
                })
                .build()
                ;
    }

    public ItemReader<String> reader(){
        List<String> data = Arrays.asList("a", "b", "c");
        return new InputItemReader(data);

    }

    class InputItemReader implements ItemReader<String> {


        private final Iterator<String> iterator;

        public InputItemReader(List<String> datataList) {
            this.iterator = datataList.iterator();
        }

        @Override
        public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            if (iterator.hasNext()){
                return iterator.next();
            }else {
                return null;
            }
        }
    }
}


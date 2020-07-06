package com.kingcall.batch.job.errorHadle;

import com.kingcall.batch.job.itemReaderAndWriter.reader.BaseBatchItemReader;
import com.kingcall.batch.utils.CommonConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
@Configuration
@EnableBatchProcessing
public class RetryJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    CommonConsoleItemWriter writer;

    @Bean
    public Job retryDemoJob() {
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
                .<String,String>chunk(1)
                .reader(reader())
                .processor(process())
                .writer(writer)
                .faultTolerant()
                .retry(RuntimeException.class)// If a retry limit is provided then retryable exceptions must also be specified
                .retryLimit(2)
                .build()
                ;
    }

    public ItemProcessor process(){
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String s) throws Exception {
                int attemptCount = 0;
                System.out.println("processing item " + s);
                if (s.equalsIgnoreCase("6")) {
                    attemptCount++;
                    if (attemptCount >= 3) {
                        System.out.println("Retried " + attemptCount + " times success .");
                        return String.valueOf(Integer.valueOf(s) * -1);
                    } else {
                        System.out.println("Processed " + attemptCount + " times fail .");
                        throw new RuntimeException("retry exception");

                    }
                } else {
                    return String.valueOf(Integer.valueOf(s) * -1);
                }
            }
        };
    }

    public ItemReader<String> reader(){
        List<String> data = Arrays.asList("1", "2", "3","4","5","6","7","8");
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

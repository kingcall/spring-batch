package com.kingcall.batch.job.jobParameters;

import com.kingcall.batch.utils.CommonConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//@Configuration
//@EnableBatchProcessing
public class StepParameterPassWithStepListenerJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    CommonConsoleItemWriter writer;

    @Bean
    public Job passParameterToStepJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(passParameterToStep())
                .build()
                ;
    }

    int filter;



    @Bean
    public Step passParameterToStep() {
        return stepBuilders
                .get("passParameterToStep")
                .<String, String>chunk(1)
                .reader(reader())
                .processor(process())
                 .writer(writer)
                .listener(new MyChunkListener())
                .build()
                ;
    }

    public ItemProcessor process() {
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String s) throws Exception {
                System.out.println("processing item " + s);
                return s;
            }
        };
    }

    public ItemReader<String> reader() {
        List<String> data = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
        return new InputItemReader(data);

    }


    class InputItemReader implements ItemReader<String> {
        private final Iterator<String> iterator;

        public InputItemReader(List<String> datataList) {
            this.iterator = datataList.iterator();
        }

        @Override
        public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return null;
            }
        }
    }

    class MyChunkListener implements StepListener {
        @BeforeStep
        public void beforeStep() {
            // 在这个可以赋值给全局变量，然后使用

            System.out.println(String.format("============================== %s beforeStep ===================================", ""));
        }

        @AfterStep
        public void afterStep() {
            System.out.println(String.format("============================== %s afterStep ===================================", ""));
        }
    }


}

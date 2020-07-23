package com.kingcall.batch.job.jobParameters;


import com.kingcall.batch.utils.CommonConsoleItemWriter;
import com.kingcall.batch.utils.PatientItemReader;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;

import java.util.Map;

/**
 * info=kingcall 传递给程序
 */

//@Configuration
//@EnableBatchProcessing
public class StepParametersApplicationJob2 implements StepExecutionListener {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilders;

    Map<String, JobParameter> parameters;

    private static String path = null;

    @Autowired
    CommonConsoleItemWriter itemWriter;

    @Autowired
    @StepScope
    ItemReader<String> reader;


    @Bean
    public Job parametersJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(stepParametersJobStep1())
                .next(stepParametersJobStep2())
                .build()
                ;
    }

    @Bean
    public Step stepParametersJobStep1() {
        return stepBuilders
                .get("stepParametersJobStep1")
                .listener(this)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(String.format("hello spring batch parameters %s", parameters.get("info")));
                        StepContext stepContext = chunkContext.getStepContext();
                        StepExecution stepExecution = stepContext.getStepExecution();
                        JobExecution jobExecution = stepExecution.getJobExecution();
                        ExecutionContext jobContext = jobExecution.getExecutionContext();
                        System.out.println("在运行过程中向程序传参数文件名字");
                        jobContext.put("pathToFile", "C:\\workspace\\autosweep\\flatfiles\\in_batch_date.dat");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build()
                ;
    }

    /**
     * 获取上一步传的参数
     *
     * @return
     */
    @Bean(name = "stepParametersJobStep2")
    public Step stepParametersJobStep2() {
        return stepBuilders
                .get("stepParametersJobStep2")
                .<String, String>chunk(1)
                .reader(reader)
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String s) throws Exception {
                        return s;
                    }
                })
                .writer(itemWriter)
                .build()
                ;
    }
    @StepScope
    @Bean
    public ItemReader<String> flatFileItemReader(@Value("#{jobExecutionContext[pathToFile]}") String path){
        System.out.println(String.format("获取到的执行时参数 path:%s", path));
        FlatFileItemReader<String> itemReader = new FlatFileItemReader<String>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setResource(new FileSystemResource(path));
        itemReader.open(new ExecutionContext());
        return itemReader;
    }


    @Bean
    public LineMapper<String> lineMapper(){
        DefaultLineMapper<String> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("\n");
        tokenizer = new DelimitedLineTokenizer("\n");
        tokenizer.setNames(new String[]{"all"});
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            return fieldSet.readString("all");
        });
        lineMapper.setLineTokenizer(tokenizer);
        return lineMapper;
    }



    @Override
    public void beforeStep(StepExecution stepExecution) {
        // 从外部的输入在运行的时候使用
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println(String.format("获取到的运行时参数: %s",stepExecution.getJobExecution().getExecutionContext().get("pathToFile").toString()));
        return null;
    }
}

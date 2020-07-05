package com.kingcall.batch.Processor;

import com.kingcall.batch.models.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


@Slf4j
@Component("restarItemReader")
public class RestarItemReader implements ItemStreamReader<Person> {
    private Long curLine = 0L;
    private boolean restart = false;

    private FlatFileItemReader<Person> reader = new FlatFileItemReader<>();

    private ExecutionContext executionContext;

    public RestarItemReader() {
        reader.setResource(new ClassPathResource("csv/person.csv"));
        // 不知道为什么第一行没跳过
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id", "firstName", "lastName"});
        tokenizer.setDelimiter(",");

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper((fieldSet -> {
            return Person.builder()
                    .id(fieldSet.readInt("id"))
                    .firstName(fieldSet.readString("firstName"))
                    .lastName(fieldSet.readString("lastName"))
                    .build();
        }));
        lineMapper.afterPropertiesSet();
        reader.setLineMapper(lineMapper);
    }

    @Override
    public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        Person person = null;

        this.curLine++;
        //如果是重启，则从上一步读取的行数继续往下执行
        if (restart) {
            reader.setLinesToSkip(this.curLine.intValue() - 1);
            restart = false;
            System.out.println("Start reading from line: " + this.curLine);
        }

        reader.open(this.executionContext);
        person = reader.read();
        //当匹配到wrongName时，显示抛出异常，终止程序
        if (person != null) {
            if (person.getFirstName().equals("wrongName"))
                throw new RuntimeException("Something wrong. Customer id: " + person.getId());
        } else {
            curLine--;
        }
        return person;
    }

    /**
     * 判断是否是重启job
     *
     * @param executionContext
     * @throws ItemStreamException
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        if (executionContext.containsKey("curLine")) {
            this.curLine = executionContext.getLong("curLine");
            this.restart = true;
        } else {
            this.curLine = 0L;
            executionContext.put("curLine", this.curLine.intValue());
        }
        log.info("init complete ....");

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("update curLine: " + this.curLine);
        executionContext.put("curLine", this.curLine);
    }

    @Override
    public void close() throws ItemStreamException {

    }

}

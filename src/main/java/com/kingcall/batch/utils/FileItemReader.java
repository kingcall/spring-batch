package com.kingcall.batch.utils;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

public class FileItemReader extends FlatFileItemReader<String> {
    DefaultLineMapper<String> lineMapper;
    DelimitedLineTokenizer tokenizer;

    public FileItemReader(String filePath) {
        init();
        setLineMapper(lineMapper);
        setResource(new FileSystemResource(filePath));
    }

    public void init() {
        lineMapper = new DefaultLineMapper<>();
        tokenizer = new DelimitedLineTokenizer("\n");
        tokenizer.setNames(new String[]{"all"});
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            return fieldSet.readString("all");
        });
        lineMapper.setLineTokenizer(tokenizer);
    }


}

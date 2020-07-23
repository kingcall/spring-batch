package com.kingcall.batch.utils;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;


public class FileItemWriter extends FlatFileItemWriter<String> {
    private String targetPath;
    public FileItemWriter(String targetPath) {
        this.targetPath = targetPath;
        setResource(new FileSystemResource(targetPath));
        setLineAggregator(new LineAggregator<String>(){
            @Override
            public String aggregate(String s) {
                return s;
            }
        });
    }

    @Override
    public void setResource(Resource resource) {
        super.setResource(resource);
    }

    @Override
    public void setLineAggregator(LineAggregator<String> lineAggregator) {
        super.setLineAggregator(lineAggregator);
    }


}

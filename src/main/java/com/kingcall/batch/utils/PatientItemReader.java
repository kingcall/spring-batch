package com.kingcall.batch.utils;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PatientItemReader extends FlatFileItemWriter<String> {
    private String targetPath;

    public PatientItemReader(@Value("#{jobParameters[pathToFile]}") String targetPath) {
        this.targetPath = targetPath;
        setResource(new FileSystemResource(targetPath));
        setLineAggregator(new LineAggregator<String>() {
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


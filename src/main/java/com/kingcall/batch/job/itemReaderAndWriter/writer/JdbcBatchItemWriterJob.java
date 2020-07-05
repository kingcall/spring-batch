package com.kingcall.batch.job.itemReaderAndWriter.writer;

import com.kingcall.batch.models.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
//@EnableBatchProcessing
public class JdbcBatchItemWriterJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;


    @Bean
    public Job jdbcItemWriterJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(jdbcItemWriterJobStep())
                .build()
                ;
    }

    @Bean
    public Step jdbcItemWriterJobStep() {
        return stepBuilders
                .get("jdbcItemWriterJobStep")
                .<Person, Person>chunk(1)
                .reader(jdbcReader())
                .writer(jdbcWriter())
                .build()
                ;
    }

    @Bean
    public JdbcBatchItemWriter<Person> jdbcWriter(){
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("insert into person2(id,firstname,lastname) values(:id,:firstName,:lastName)");
            writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
            return writer;
        }

        @Bean
        public JdbcPagingItemReader<Person> jdbcReader() {
            JdbcPagingItemReader<Person> jdbcPagingItemReader = new JdbcPagingItemReader<>();
            jdbcPagingItemReader.setDataSource(dataSource);
            jdbcPagingItemReader.setFetchSize(100);
            jdbcPagingItemReader.setRowMapper(((resultSet, row) -> {
                return Person
                        .builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("firstname"))
                        .lastName(resultSet.getString("lastname"))
                    .build();
        }));
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setFromClause("from person");
        provider.setSelectClause("id,firstName,lastname");
        Map<String, Order> order = new HashMap<> ();
        order.put("id", Order.DESCENDING);
        provider.setSortKeys(order);
        jdbcPagingItemReader.setQueryProvider(provider);
        return jdbcPagingItemReader;
    }
}


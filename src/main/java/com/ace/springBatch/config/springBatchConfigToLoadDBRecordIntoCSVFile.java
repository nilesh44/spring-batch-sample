package com.ace.springBatch.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort.Direction;

import com.ace.springBatch.batch.vo.Mobile;
import com.ace.springBatch.entity.Phone;
import com.ace.springBatch.repo.MobileRepository;

@Configuration
public class springBatchConfigToLoadDBRecordIntoCSVFile {

	@Autowired
	private MobileRepository mobileRepository;
	
	@Bean(name = "dbCsvjob")
	public Job dbCsvjob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			@Qualifier("dbCsvreader") ItemReader<Phone> reader,
			@Qualifier("dbCsvProcessor") ItemProcessor<Phone, Mobile> processor,
			@Qualifier("dbCsvWriter") ItemWriter<Mobile> writer) {
		// Job can Have Multiple Step
		// A step consist of reader processor and writer
		Step step = stepBuilderFactory.get("Load_mobile_from_db_to_csv_step").<Phone, Mobile>chunk(5).reader(reader)
				.processor(processor).writer(writer).build();

		Job job = jobBuilderFactory.get("Load_mobile_from_db_to_csv_Job").incrementer(new RunIdIncrementer())
				.start(step).build();
		return job;
	}

	//read from DB
	@Bean(name = "dbCsvreader")
	public RepositoryItemReader<Phone> dbCsvreader() {
		RepositoryItemReader<Phone> reader = new RepositoryItemReader<>();
		reader.setRepository(mobileRepository);

		reader.setMethodName("getPhone");

		// List<Object> queryMethodArguments = new ArrayList<>();
		// for status
		// queryMethodArguments.add("");
		// for emailVerified
		// queryMethodArguments.add(Boolean.TRUE);

		// reader.setArguments(queryMethodArguments);

		reader.setPageSize(5);

		Map<String, Direction> sorts = new HashMap<>();
		sorts.put("id", Direction.ASC);
		reader.setSort(sorts);
		 

		return reader;
	}

	//write into CSV file
	@Bean(name = "dbCsvWriter")
	public FlatFileItemWriter<Mobile> dbCsvWriter() {

		
		FlatFileItemWriter<Mobile> writer = new FlatFileItemWriter<Mobile>();
    	//writer.setResource(new ClassPathResource("bdcsvphone.csv"));
		writer.setResource(new FileSystemResource(new File("D:\\bdcsvphone.csv")));
		
    	DelimitedLineAggregator<Mobile> delLineAgg = new DelimitedLineAggregator<Mobile>();
    	delLineAgg.setDelimiter(",");
    	
    	BeanWrapperFieldExtractor<Mobile> fieldExtractor = new BeanWrapperFieldExtractor<Mobile>();
    	fieldExtractor.setNames(new String[] {"employeeId", "mobileNumber","prefered"});
    	
    	delLineAgg.setFieldExtractor(fieldExtractor);
    	
    	writer.setLineAggregator(delLineAgg);
        writer.setAppendAllowed(true);
        writer.setShouldDeleteIfEmpty(true);
        writer.setShouldDeleteIfExists(true);
        
        return writer;
        
	}

	public DelimitedLineAggregator<Mobile> getDelimitedLineAggregator() {
		BeanWrapperFieldExtractor<Mobile> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<Mobile>();
		beanWrapperFieldExtractor.setNames(new String[] { "employeeId", "mobileNumber", "prefered" });

		DelimitedLineAggregator<Mobile> delimitedLineAggregator = new DelimitedLineAggregator<Mobile>();
		delimitedLineAggregator.setDelimiter(",");
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		return delimitedLineAggregator;

	}
}

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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort.Direction;

import com.ace.springBatch.batch.vo.Mobile;
import com.ace.springBatch.entity.Phone;
import com.ace.springBatch.repo.MobileRepository;

@Configuration
@EnableBatchProcessing
public class springBatchConfiguration {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private MobileRepository mobileRepository;

	@Bean(name = "csvDbjob")
	public Job csvDbjob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			@Qualifier("csvDbMobileReder") ItemReader<Mobile> reader,
			@Qualifier("csvDbMobileProcessor") ItemProcessor<Mobile, Phone> processor,
			@Qualifier("csvDbMobileWriter") ItemWriter<Phone> writer) {
		// Job can Have Multiple Step
		// A step consist of reader processor and writer
		Step step = stepBuilderFactory.get("ETL_load_mobile").<Mobile, Phone>chunk(5).reader(reader)
				.processor(processor).writer(writer).build();

		Job job = jobBuilderFactory.get("ETL_load_mobile").incrementer(new RunIdIncrementer()).start(step).build();
		return job;
	}

	@Bean(name = "csvDbMobileReder")
	public FlatFileItemReader<Mobile> csvDbMobileReder() {
		final Resource resource = resourceLoader.getResource("classpath:phone.csv");

		FlatFileItemReader<Mobile> flatFileItemReader = new FlatFileItemReader<Mobile>();
		flatFileItemReader.setName("csv-reder");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setLineMapper(csvDblineMapper());
		return flatFileItemReader;
	}

	private LineMapper<Mobile> csvDblineMapper() {
		DefaultLineMapper<Mobile> defaultLineMapper = new DefaultLineMapper<Mobile>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "employeeId", "mobileNumber", "prefered" });
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		BeanWrapperFieldSetMapper<Mobile> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Mobile>();
		beanWrapperFieldSetMapper.setTargetType(Mobile.class);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;
	}

	// DBCSV

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
        
		/*
		 * final Resource resource =
		 * resourceLoader.getResource("classpath:bdcsvphone.csv");
		 * 
		 * return new FlatFileItemWriterBuilder<Mobile>() .name("studentWriter")
		 * .lineAggregator(getDelimitedLineAggregator()) .resource(resource) .build();
		 */
		
		/*
		 * FlatFileItemWriter<Mobile> flatFileItemWriter = new
		 * FlatFileItemWriterBuilder<Mobile>();
		 * flatFileItemWriter.setResource(resource);
		 * flatFileItemWriter.setName("csv-writer");
		 * flatFileItemWriter.setAppendAllowed(true); //
		 * flatFileItemWriter.setLineAggregator(getDelimitedLineAggregator());
		 * 
		 * flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Mobile>() {
		 * { setDelimiter(","); setFieldExtractor(new
		 * BeanWrapperFieldExtractor<Mobile>() { { setNames(new String[] { "employeeId",
		 * "mobileNumber", "prefered" }); } }); } });
		 * 
		 * return flatFileItemWriter;
		 */
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

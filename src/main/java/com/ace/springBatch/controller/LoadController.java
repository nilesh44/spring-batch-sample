package com.ace.springBatch.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("csvDbjob")
	Job csvDbjob;
	
	@Autowired
	@Qualifier("dbCsvjob")
	Job dbCsvjob;
	
	@GetMapping("/phone")
	public BatchStatus loadFile() {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(parameters );
		JobExecution jobExecution = null;
		try {
			jobExecution=jobLauncher.run(csvDbjob, jobParameters);
			
			while(jobExecution.isRunning()) {
				System.out.println("batch is running .................");
			}
			
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jobExecution.getStatus();
		
	}

	@GetMapping("/phone/dbCsvjob")
	public BatchStatus loadMobileDBtoCsvFile() {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(parameters );
		JobExecution jobExecution = null;
		try {
			jobExecution=jobLauncher.run(dbCsvjob, jobParameters);
			
			while(jobExecution.isRunning()) {
				System.out.println(" dbCsvjob batch is running .................");
			}
			
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jobExecution.getStatus();
		
	}
}

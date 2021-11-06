package com.ace.springBatch.batch;

import java.sql.Timestamp;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.ace.springBatch.batch.vo.Mobile;
import com.ace.springBatch.entity.Phone;


@Component("csvDbMobileProcessor")
public class CsvDbMobileProcessor implements ItemProcessor<Mobile, Phone>{

	@Override
	public Phone process(Mobile mobile) throws Exception {
		
		return Phone
				.builder()
				.employeeId(Integer.valueOf(mobile.getEmployeeId()))
		        .mobileNumber(mobile.getMobileNumber())
		        .preferred(mobile.getPrefered())
		        .createTimestamp(new Timestamp(System.currentTimeMillis()))
		        .build();
		
	}

}

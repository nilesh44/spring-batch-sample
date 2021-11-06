package com.ace.springBatch.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.ace.springBatch.batch.vo.Mobile;
import com.ace.springBatch.entity.Phone;

import lombok.extern.slf4j.Slf4j;

@Component("dbCsvProcessor")
@Slf4j
public class DbCsvProcessor implements ItemProcessor<Phone, Mobile> {

	@Override
	public Mobile process(Phone item) throws Exception {
		log.info("dbCsvProcessor ........................."+ item);
		return Mobile
				.builder()
				.employeeId(item.getEmployeeId().toString())
				.mobileNumber(item.getMobileNumber())
				.prefered(item.getPreferred())
				.build();

	}

}

package com.ace.springBatch.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ace.springBatch.entity.Phone;
import com.ace.springBatch.repo.MobileRepository;

@Component("csvDbMobileWriter")
public class CsvDbMobileWriter implements ItemWriter<Phone>{
@Autowired
MobileRepository mobileRepository;
	
	@Override
	public void write(List<? extends Phone> mobiles) throws Exception {
		
			mobileRepository.saveAll(mobiles);
		
	}
	
	

}

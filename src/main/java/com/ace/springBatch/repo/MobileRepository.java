package com.ace.springBatch.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ace.springBatch.entity.Phone;

@Repository
public interface MobileRepository extends JpaRepository<Phone, Integer>{

	
	@Query("SELECT p FROM Phone p")
	public Page<Phone> getPhone(Pageable pageable) ;
}

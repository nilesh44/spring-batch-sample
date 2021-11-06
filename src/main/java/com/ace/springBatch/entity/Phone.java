package com.ace.springBatch.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="mobile")
public class Phone {
	
	@Id
	@Column(name="mobile_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer mobileId;
	
	@Column(name="emp_id")
	private Integer employeeId;
	
	@Column(name="mob_num")
	private String mobileNumber;
	
	@Column(name="block")
	private String block;
	
	@Column(name="prefered")
	private String preferred;
	
	@Column(name="crt_tms")
	private Timestamp createTimestamp;
	
	@Column(name="exp_tms")
	private Timestamp expireTimestamp;

}

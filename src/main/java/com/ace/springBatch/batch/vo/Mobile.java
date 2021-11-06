package com.ace.springBatch.batch.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mobile  implements Serializable {

	private static final long serialVersionUID = 5691222947098538288L;

	private String employeeId;
	
	private String mobileNumber;
	
	private String prefered;
}

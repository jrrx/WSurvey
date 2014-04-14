package com.voyage.module.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BaseDataService {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

}

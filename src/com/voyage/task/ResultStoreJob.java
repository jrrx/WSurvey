package com.voyage.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.voyage.core.SurveyStatus;
import com.voyage.util.LogUtil;
import com.voyage.util.Utils;

@Component
public class ResultStoreJob {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static Object lock = new Object();
	private static List<SurveyStatus> sses;

	public static void storeResult(SurveyStatus ss) {
		synchronized (lock) {
			if (sses == null) {
				sses = new ArrayList<SurveyStatus>();
			}
			sses.add(ss);
		}
	}

	// cron = "0/5 * * * * *" 表示每5秒钟执行一次
	// cron = "0 0/1 * * * *" 表示每1分钟执行一次
	@Scheduled(cron = "0 0/3 * * * *")
	public void storeResultJob() {
		try {
			LogUtil.GlobalLog.info("Begin storeResultJob...");
			
			List<SurveyStatus> temp = null;
			synchronized (lock) {
				temp = sses;
				sses = new ArrayList<SurveyStatus>();
			}
			

			if (temp != null && temp.size() > 0) {
				
				LogUtil.GlobalLog.info("Result size is:"+ temp.size());
				Timestamp curTime = Utils.getInstance().getCurrentTime();
				
				
				for (SurveyStatus surveyStatus : temp) {
					// 删除已有的答案
					String sql = "delete from t_result where paperid = ? and useropenid = ?";
					jdbcTemplate.update(sql, surveyStatus.getPaperId(),
							surveyStatus.getUserOpenId());

					// 存储用户回答的答案
					String insertsql = "insert into t_result (paperid, useropenid, questionpos, answertext, answertime) values (?,?,?,?,?)";
					
					List<Object[]> args = new ArrayList<Object[]>();
					Map<Integer, String> replies = surveyStatus.getReplies();
					Set<Entry<Integer, String>> ents = replies.entrySet();
					for (Entry<Integer, String> ent : ents) {
						Integer pos = ent.getKey();
						String reply = ent.getValue();

						Object[] arg = new Object[5];
						arg[0] = surveyStatus.getPaperId();
						arg[1] = surveyStatus.getUserOpenId();
						arg[2] = pos;
						arg[3] = reply;
						arg[4] = curTime;

						// for (Object obj : arg) {
						// LogUtil.GlobalLog.info("The args is:" + obj);
						// }
						// LogUtil.GlobalLog.info("The args item, \r\n");

						args.add(arg);
					}

					jdbcTemplate.batchUpdate(insertsql, args);
				}
			}

		} catch (Exception e) {
			LogUtil.GlobalLog.error(" Store Result Job error:",  e);
		}
		
		LogUtil.GlobalLog.info("End storeResultJob...");

	}

}

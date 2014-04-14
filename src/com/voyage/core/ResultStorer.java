package com.voyage.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.voyage.util.LogUtil;

@Component
public class ResultStorer {

	@Autowired
	private static JdbcTemplate jdbcTemplate;

	public void storeResult(SurveyStatus surveyStatus) {
		Runnable target = new ResultRunnable(surveyStatus);
		new Thread(target).start();
	}

	static class ResultRunnable implements Runnable {
		// private int paperid;
		// private int fanid;
		// private Map<String, String> replies;

		// public ResultStorer(int paperid, int fanid, Map<String, String>
		// replies){
		// this.paperid = paperid;
		// this.fanid = fanid;
		// this.replies = replies;
		// }

		private SurveyStatus surveyStatus;

		public ResultRunnable(SurveyStatus surveyStatus) {
			this.surveyStatus = surveyStatus;
		}

		@Override
		public void run() {

			try { 
				LogUtil.GlobalLog
						.info("Begin to store surveyStatus in ResultStorer with surveyStatus:"
								+ surveyStatus.getReplies().size());

				if (this.surveyStatus != null) {
					// 删除已有的答案
					String sql = "delete from t_result where paperid = ? and fanid = ?";
					jdbcTemplate.update(sql, surveyStatus.getPaperId(),
							surveyStatus.getUserOpenId());

					// 存储用户回答的答案
					String insertsql = "insert into t_result (paperid, fanid, questionpos, answertext) values (?,?,?,?)";

					List<Object[]> args = new ArrayList<Object[]>();
					Map<Integer, String> replies = surveyStatus.getReplies();
					Set<Entry<Integer, String>> ents = replies.entrySet();
					for (Entry<Integer, String> ent : ents) {
						Integer pos = ent.getKey();
						String reply = ent.getValue();

						Object[] arg = new Object[4];
						arg[0] = surveyStatus.getPaperId();
						arg[1] = surveyStatus.getUserOpenId();
						arg[2] = pos;
						arg[3] = reply;

						for (Object obj : arg) {
							LogUtil.GlobalLog.info("The args is:" + obj);
						}
						LogUtil.GlobalLog.info("The args item, \r\n");

						args.add(arg);
					}

					jdbcTemplate.batchUpdate(insertsql, args);

				}
			} catch (DataAccessException e) {
				LogUtil.GlobalLog.error("Insert user replies error:", e);
			}

		}

	}
}

package com.voyage.init;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyage.cache.SurveyCache;
import com.voyage.core.PaperLoader;
import com.voyage.core.WxService;
import com.voyage.meta.db.Answer;
import com.voyage.meta.db.Paper;
import com.voyage.meta.db.Question;
import com.voyage.meta.paper.Judger;
import com.voyage.util.FileFinder;
import com.voyage.util.LogUtil;
import com.voyage.util.Utils;

@Controller
@RequestMapping("/doTest")
public class TestController {

	@Autowired
	private PaperLoader paperLoader;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SurveyCache surveyCache;
	
	@Autowired
	private WxService wxService;
	
	@RequestMapping("/testCache")
	public void testCache(){
		Paper paper = this.surveyCache.getPaper("1");
		System.err.println(paper);
	}
	
	@RequestMapping("/testMsg")
	public void testSendTextMsg(){
		this.wxService.sendTextMsg("oP0HgjtgXdEq3Izvaw9_y8CpBmso", "你好 你好 测试");
	}
	

	//@RequestMapping("/loadPaper")
	public void loadPaper2() throws Exception {
		
		String sql = "update t_question set content = ? where id=11 and paperid=2";
		String content = "一、您认为下列各项在您选择在本商场中购物重要吗？1/17.本商场的知名度";
		
		this.jdbcTemplate.update(sql, content);
		
		//surveyCache.initCache();
		//this.loadPaper2_do();
	}
	
	
	private void loadPaper2_do() throws Exception{
		File file = FileFinder.findFile("resources/temp/survey2.paper");
		Paper p = this.paperLoader.loadPaperFromFile(file);

		p.setTitle("顾客满意度调研问卷");
		p.setPaperstatus(1);// 可用状态
		p.setPapertype(Judger.quetype_single_choose);// 单选题目
		p.setCreatetime(Utils.getInstance().getCurrentTime());
		p.setLastmodifytime(p.getCreatetime());
		p.setAuthorid(1);// 创建者id

		Utils utils = Utils.getInstance();
		StringBuilder insertSql = new StringBuilder();
		insertSql
				.append("INSERT INTO t_paper (title,paperstatus,papertype,createtime,lastmodifytime,authorid) VALUES ");
		insertSql.append("('" + p.getTitle() + "'," + p.getPaperstatus() + ","
				+ p.getPapertype() + ",'" + utils.getTimeStr(p.getCreatetime())
				+ "','" + utils.getTimeStr(p.getLastmodifytime()) + "',"
				+ p.getAuthorid() + ")");

		this.jdbcTemplate.update(insertSql.toString());

		List<Question> ques = p.getQuestions();
		String qinsert = "INSERT INTO t_question (content,paperid,position,typeid) VALUES (?,?,?,?)";
		String ainsert = "INSERT INTO t_answer (questionid,position,content) VALUES (?,?,?)";
		
		
		int a = 2;// a is paper id
		// get the max question id from DB TODO 
		int i = 11;
		for (Question q : ques) {
			this.jdbcTemplate.update(qinsert, q.getContent(), 2, i, 1);
			List<Answer> ass = q.getAnswers();

			int j = 1;// j is position
			for (Answer as : ass) {
				this.jdbcTemplate.update(ainsert, i, j, as.getContent());
				j++;
			}

			i++;
		}
		// 设置二维码
		this.jdbcTemplate
				.update("INSERT INTO t_qrcode_paper (sceneid, paperid) VALUES ('1', '2')");
	}
	
	
	
	public void loadPaper1() throws Exception {
		
		surveyCache.initCache();
		
		// Init cacheManager with XML configuration
		CacheManager cm = CacheManager.newInstance(new FileInputStream(
				FileFinder.findFile("ehcache/ehcache.xml")));
		String[] names = cm.getCacheNames();
		LogUtil.GlobalLog.info("Begin to init cache:" + Arrays.toString(names));

		// SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate);
		// simpleInsert.withTableName("t_paper");

		File file = FileFinder.findFile("resources/temp/survey1.paper");
		Paper p = this.paperLoader.loadPaperFromFile(file);

		p.setTitle("调查试题样例1");
		p.setPaperstatus(1);// 可用状态
		p.setPapertype(1);// 单选题目
		p.setCreatetime(Utils.getInstance().getCurrentTime());
		p.setLastmodifytime(p.getCreatetime());
		p.setAuthorid(1);// 创建者id

		Utils utils = Utils.getInstance();
		StringBuilder insertSql = new StringBuilder();
		insertSql
				.append("INSERT INTO t_paper (title,paperstatus,papertype,createtime,lastmodifytime,authorid) VALUES ");
		insertSql.append("('" + p.getTitle() + "'," + p.getPaperstatus() + ","
				+ p.getPapertype() + ",'" + utils.getTimeStr(p.getCreatetime())
				+ "','" + utils.getTimeStr(p.getLastmodifytime()) + "',"
				+ p.getAuthorid() + ")");

		this.jdbcTemplate.update(insertSql.toString());

		List<Question> ques = p.getQuestions();
		String qinsert = "INSERT INTO t_question (content,paperid,position,typeid) VALUES (?,?,?,?)";
		String ainsert = "INSERT INTO t_answer (questionid,position,content) VALUES (?,?,?)";

		int i = 1;
		for (Question q : ques) {
			this.jdbcTemplate.update(qinsert, q.getContent(), 1, i, 1);
			List<Answer> ass = q.getAnswers();

			int j = 1;
			for (Answer as : ass) {
				this.jdbcTemplate.update(ainsert, i, j, as.getContent());
				j++;
			}

			i++;
		}

		// 设置二维码
		this.jdbcTemplate
				.update("INSERT INTO t_qrcode_paper (sceneid, paperid) VALUES ('1', '1')");

	}

	@RequestMapping("/storeResult")
	public void testStoreResult(){
		
	}

}

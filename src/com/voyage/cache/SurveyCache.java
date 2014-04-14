package com.voyage.cache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.voyage.core.PaperLoader;
import com.voyage.core.SurveyStatus;
import com.voyage.meta.db.Answer;
import com.voyage.meta.db.Paper;
import com.voyage.meta.db.QrcodePaper;
import com.voyage.meta.db.Question;
import com.voyage.util.FileFinder;
import com.voyage.util.LogUtil;

/**
 * TODO 需要和数据库存储相结合，目前只是满足Demo
 * 
 * @author Houyangyang
 */
@Component
public class SurveyCache {

	@Autowired
	private PaperLoader paperLoader;

	public static void main(String[] args) throws Exception {

		// CacheManager manager = CacheManager.newInstance(new FileInputStream(
		// FileFinder.findFile("ehcache/ehcache.xml")));
		// String[] names = manager.getCacheNames();
		// System.out.println(Arrays.toString(names));
		SurveyCache surveyCache = new SurveyCache();
		surveyCache.paperLoader = new PaperLoader();
		surveyCache.initCache();

	}

	private CacheManager cm = null;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * server在启动时候，加载问卷到内存缓存
	 * 
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 * @throws CacheException
	 */
	public void initCache() throws Exception {

		cm = CacheManager.newInstance(new FileInputStream(FileFinder
				.findFile("ehcache/ehcache.xml")));

		// 此段代码需要优化，临时这么写

		// 加载问卷
		List<Paper> papers = this.jdbcTemplate.query(
				"select * from t_paper where paperstatus = 1",
				ParameterizedBeanPropertyRowMapper.newInstance(Paper.class));
		for (Paper paper : papers) {
			List<Question> questions = this.jdbcTemplate
					.query("select * from t_question where paperid = ? order by position",
							ParameterizedBeanPropertyRowMapper
									.newInstance(Question.class), paper.getId());
			for (Question question : questions) {
				List<Answer> answers = this.jdbcTemplate
						.query("select * from t_answer where questionid = ? order by position",
								ParameterizedBeanPropertyRowMapper
										.newInstance(Answer.class), question
										.getId());
				question.setAnswers(answers);
			}
			paper.setQuestions(questions);
		}

		// 加载问卷 和 二维码的对应关系
		List<QrcodePaper> qps = this.jdbcTemplate
				.query("select * from t_qrcode_paper where paperid != -1 order by paperid",
						ParameterizedBeanPropertyRowMapper
								.newInstance(QrcodePaper.class));

		// 将对应关系放入缓存
		for (Paper paper : papers) {
			for (QrcodePaper qp : qps) {
				if (paper.getId() == qp.getPaperid()) {
					LogUtil.GlobalLog.info("cache the sceneId is:"
							+ qp.getSceneid() + ","
							+ paper.getQuestions().size());
					this.addPaper(String.valueOf(qp.getSceneid()), paper);
				}
			}
		}

	}

	public Paper getPaper(String sceneId) {
		Cache cache = this.getPaperCache();
		Element ele = cache.get(sceneId);
		if (ele != null) {
			Object re = ele.getObjectValue();
			return re == null ? null : (Paper) re;
		}
		return null;
	}

	public void addPaper(String sceneId, Paper paper) {
		Element ele = new Element(sceneId, paper);
		this.getPaperCache().put(ele);
	}

	public SurveyStatus getSurveyStatus(String userId) {
		Cache cache = this.getSurveyStatusCache();
		Element ele = cache.get(userId);
		if (ele != null) {
			Object re = ele.getObjectValue();
			return re == null ? null : (SurveyStatus) re;
		}
		return null;
	}

	public void addSurveyStatus(SurveyStatus surveyStatus) {
		Element ele = new Element(surveyStatus.getUserOpenId(), surveyStatus);
		this.getSurveyStatusCache().put(ele);
	}

	public void removeSurveyStatus(String userId) {
		this.getSurveyStatusCache().remove(userId);
	}

	private Cache getPaperCache() {
		return cm.getCache("paperCache");
	}

	public Cache getSurveyStatusCache() {
		return cm.getCache("surveyStatusCache");
	}

}

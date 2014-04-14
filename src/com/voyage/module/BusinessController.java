package com.voyage.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.voyage.module.result.ResultService;
import com.voyage.util.LogUtil;

@Controller
public class BusinessController {

	@Autowired
	private ResultService resultService;

	@RequestMapping("/testResult")
	public void testExportSurveyResult(
	// @RequestParam("paperId") int paperId,
			HttpServletResponse response) throws IOException {
		// List<Result> res = this.jdbcTemplate.query("select * from t_result",
		// ParameterizedBeanPropertyRowMapper.newInstance(Result.class));
		// System.out.println(res.size());

		// this.resultService.testGenerateResultExcel(2);

		response.getWriter().write("Hello you test.");

	}

	@RequestMapping("/exportResult")
	public void exportSurveyResult(@RequestParam("paperId") int paperId,
			HttpServletResponse response) {
		File re = null;
		try {
			LogUtil.GlobalLog
					.info("Export paper result, paperId is:" + paperId);
			re = this.resultService.generateResultExcel(paperId);
			if (re != null) {
				response.setContentType("application/octet-stream");
				// response.setHeader("Content-disposition","attachment;filename="+re.getName()+".xlsx");
				response.addHeader(
						"Content-Disposition",
						"attachment;filename=\""
								+ URLEncoder.encode(re.getName(), "UTF-8")
								+ "\"");

				InputStream ins = new FileInputStream(re);
				OutputStream ous = response.getOutputStream();
				IOUtils.copy(ins, ous);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (re != null) {
					re.delete();
				}
			} catch (Exception e) {
			}
		}
	}

}

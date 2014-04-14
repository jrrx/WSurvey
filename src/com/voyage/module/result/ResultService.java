package com.voyage.module.result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.voyage.conf.SurveyConfig;
import com.voyage.meta.db.Result;
import com.voyage.module.base.BaseDataService;
import com.voyage.util.ExcelUtil;
import com.voyage.util.LogUtil;
import com.voyage.util.Utils;

@Service
public class ResultService extends BaseDataService {

	public File generateResultExcel(int paperId) {

		int queSize = this.getPaperQuestionSize(paperId);

		final ExcelUtil eu = ExcelUtil.it();
		Workbook wb = eu.getResultExcelDefault();
		final Sheet st = wb.getSheetAt(0);

		// 渲染问题序号行
		Row row = eu.getRow(st, 0);
		for (int i = 1; i <= queSize; i++) {
			eu.getCell(row, i).setCellValue("Q" + i);
		}

		String sql = "select * from t_result where paperid=? order by useropenid, questionpos";
		
		Object[] args = new Object[1];
		args[0] = paperId;

		final String[] lastUsers = { null };
		final int[] rowNums = { 0 };

		this.jdbcTemplate.query(sql, args, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				if (lastUsers[0] == null) {
					lastUsers[0] = rs.getString("useropenid");
				}
				
				
				if (rowNums[0] == 0) {
					rowNums[0] = 1;
				}

				String lastUser = lastUsers[0];
				int rowNum = rowNums[0];

				// 渲染用户的答案
				Row curRow = eu.getRow(st, rowNum);
				String curUser = rs.getString("useropenid");
				if (!curUser.equals(lastUser)) {
					rowNums[0] = ++rowNum;
					lastUsers[0] = curUser;
					LogUtil.GlobalLog.info("rowNums[0]:"+rowNums[0]+",lastUsers[0]:"+lastUsers[0]);
					curRow = eu.getRow(st, rowNum);
				}
				eu.getCell(curRow, rs.getInt("questionpos") + 1).setCellValue(
						rs.getString("answertext"));

			}
		});

		// save to file
		String serverTemp = SurveyConfig.getMsg("tomcat_temp");
		String tempName = Utils.getInstance().getCurrentTimeStr(
				"yyyyMMddHHmmss");
		String pathName = serverTemp + File.separator + tempName + ".xlsx";
		File file = new File(pathName);
		file.delete();
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(pathName);
			wb.write(out);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}

		return file;

	}

	private int getPaperQuestionSize(int paperId) {
		String sql = "select count(*) as quecount from t_question where paperid = ?";
		Map<String, Object> res = this.jdbcTemplate.queryForMap(sql, paperId);
		Object quecount = res.get("quecount");
		if (res != null && quecount != null) {
			return Integer.parseInt("" + quecount);
		}
		return 0;
	}

	public File testGenerateResultExcel(int paperId) {

		List<Result> ress = this.jdbcTemplate.query("select * from t_result",
				ParameterizedBeanPropertyRowMapper.newInstance(Result.class));
		System.out.println(ress.size());

		// TODO get paper info， get question number

		int qNum = 20;//
		//

		List<Map<String, Object>> res = this.jdbcTemplate
				.queryForList(
						"select useropenid, questionpos, answertext from t_result where paperid=? order by useropenid, questionpos",
						paperId);
		if (res.size() == 0) {
			return null;
		}

		ExcelUtil eu = ExcelUtil.it();
		Workbook wb = eu.getResultExcelDefault();
		Sheet st = wb.getSheetAt(0);

		// 渲染问题序号行
		Row row = eu.getRow(st, 0);
		for (int i = 1; i <= qNum; i++) {
			eu.getCell(row, i).setCellValue("Q" + i);
		}

		// 渲染用户的答案
		int lastUserId = (Integer) res.get(0).get("useropenid");
		int rowNum = 1;
		Row curRow = eu.getRow(st, rowNum);

		for (Map<String, Object> re : res) {
			int userid = (Integer) re.get("useropenid");
			if (userid != lastUserId) {
				rowNum++;
				curRow = eu.getRow(st, rowNum);
			}
			eu.getCell(curRow, (Integer) re.get("questionpos")).setCellValue(
					(String) re.get("answertext"));
		}

		File ex = new File("E:/bb.xlsx");
		ex.delete();
		FileOutputStream out;
		try {
			out = new FileOutputStream("E:/bb.xlsx");
			wb.write(out);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}

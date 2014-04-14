package com.voyage.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.voyage.meta.db.Answer;
import com.voyage.meta.db.Paper;
import com.voyage.meta.db.Question;
import com.voyage.util.Consts;
import com.voyage.util.FileFinder;

@Component
public class PaperLoader {

	public static void main(String[] args) throws IOException,
			URISyntaxException {
		File findFile = FileFinder.findFile("resources/temp/survey1.paper");
		// System.out.println(findFile.exists());
		new PaperLoader().loadPaperFromFile(findFile);
	}

	public Paper loadPaperFromFile(File file) throws IOException {

		Paper p = new Paper();
		p.setQuestions(new ArrayList<Question>());

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		Question q = null;
		Answer a = null;
		String line = null;
		for (line = br.readLine(); line != null; line = br.readLine()) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}
			if (!line.startsWith(Consts.CommentStr)) {
				if(line.startsWith("**")){
					q = new Question();
					q.setAnswers(new ArrayList<Answer>());
					q.setContent(line.substring(2));
					p.getQuestions().add(q);
				} else {
					a = new Answer();
					a.setContent(line);
					q.getAnswers().add(a);
				}
			}
		}

		// List<Question> qs = p.getQuestions();
		// for (Question qu : qs) {
		// System.out.println(qu.toString());
		// System.out.println("\r\n");
		// }

		br.close();
		fr.close();

		return p;
	}
}

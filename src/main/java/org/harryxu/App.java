package org.harryxu;

import org.harryxu.gui.TriviaApplication;
import org.harryxu.parser.CSVParser;
import org.harryxu.app.question.Question;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        CSVParser csvParser = new CSVParser(new File("resources/trivia.csv"));
        List<Question> questions;

        try {
            questions = csvParser.parse();
        } catch (IOException e) {
            System.out.println("An exception occurred while trying to read CSV data.");
            e.printStackTrace();
            return;
        }

        TriviaApplication application = new TriviaApplication(questions);
        application.start();
    }
}

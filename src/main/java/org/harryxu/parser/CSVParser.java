package org.harryxu.parser;

import org.harryxu.app.question.Difficulty;
import org.harryxu.app.question.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {
    private final File csvFile;

    public CSVParser(File csvFile) {
        this.csvFile = csvFile;
    }

    public List<Question> parse() throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader input = new BufferedReader(new FileReader(this.csvFile))) {
            String line;

            while ((line = input.readLine()) != null) {
                lines.add(line);
            }
        }

        List<Question> questions = new ArrayList<>();

        for (String line : lines.subList(1, lines.size())) {
            List<String> data = parseLine(line);

            questions.add(new Question(
                    Difficulty.parse(data.get(0)),
                    data.get(1),
                    data.get(2),
                    data.get(3),
                    data.subList(3, data.size())
            ));
        }

        return questions;
    }

    private static List<String> parseLine(String line) {
        List<String> data = new ArrayList<>();

        boolean inDoubleQuotes = false;
        boolean inSingleQuotes = false;
        int prevIndex = 0;

        for (int i = 0; i < line.length(); i++) {
            char prev = i == 0 ? '\0' : line.charAt(i - 1);
            char c = line.charAt(i);

            if ((c == '"') && (prev != '\\')) {
                inDoubleQuotes = !inDoubleQuotes;
            }
            if ((c == '\'') && (prev != '\\')) {
                inSingleQuotes = !inSingleQuotes;
            }

            if ((c == ',') && (prev != '\\') && (!inDoubleQuotes) && (!inSingleQuotes)) {
                data.add(line.substring(prevIndex, i));
                prevIndex = i + 1;
            }
        }

        data.add(line.substring(prevIndex));

        return data;
    }
}

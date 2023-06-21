package com.kfu.qs.web.rest;

import com.kfu.qs.service.dto.QuestionTestDTO;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MathtestResourceUtils {
    static List<QuestionTestDTO> extracted() throws Exception {
        List<QuestionTestDTO> questionTestDTOS = new ArrayList<>();
        // Get the input stream of the CSV file from the resource folder
        InputStream inputStream = CSVReader.class.getResourceAsStream("/q.csv");
        if (inputStream != null) {
            // Create a BufferedReader to read the CSV file
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the CSV line by comma
                String[] data = line.split(",");

                // Assuming the first column contains the questions and the second column contains the answers
                String question = data[0];
                String answer = data[1];
                questionTestDTOS.add(new QuestionTestDTO(question, answer));
            }

            // Close the reader
            reader.close();
        } else {
            System.out.println("Failed to load the CSV file.");
        }
        return questionTestDTOS;
    }

    public static <T> List<T> shuffleAndReduceList(List<T> originalList, int newSize) {
        List<T> copiedList = new ArrayList<>(originalList); // Create a copy of the original list


        int initialSize = copiedList.size();
        if (initialSize < newSize) {
            int diff = newSize - initialSize;
            for (int i = 0; i < diff; i++) {
                int indexToCopy = new Random().nextInt(initialSize);
                copiedList.add(copiedList.get(indexToCopy));
            }
        } else {
            // Reduce the size of the copied list
            while (copiedList.size() > newSize) {
                int indexToRemove = new Random().nextInt(copiedList.size()); // Generate a random index
                copiedList.remove(indexToRemove); // Remove the element at the random index
            }
        }

        // Randomly shuffle the values in the copied list
        Collections.shuffle(copiedList);

        return copiedList;
    }
}

package com.kfu.qs.service;

import com.kfu.qs.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class QuestionGeneratorService {

    public static final Random RANDOM = new Random();
    @Autowired
    OntoMathEduService ontoMathEduService;

    @Autowired
    MathtestService mathtestService;

    @Autowired
    PredicateService predicateService;

    public List<QuestionTestDTO> generateTest(MathtestDTO mathtestDTO) {
        // Retrieve triplets from ontology based on provided keys
        List<TripletDto> tripletDtos = ontoMathEduService.retrieveTripletsByObject(mathtestDTO.getKeys());

        // Check if enough triplets were retrieved
        if (tripletDtos.size() < mathtestDTO.getAmount()) {
            throw new IllegalArgumentException("Can't retrieve enough ontology");
        }

        // Group triplets by predicate
        Map<String, List<TripletDto>> tripletsByPredicate = tripletDtos.stream()
            .collect(Collectors.groupingBy(TripletDto::getPredicate));

        // Get predicates by RDF value
        Map<String, PredicateDTO> predicatesByRdfValue = predicateService.getByPredicate(tripletsByPredicate.keySet())
            .stream()
            .collect(Collectors.toMap(PredicateDTO::getRdfValue, Function.identity()));

        // Keep track of template usage count
        Map<TemplateDTO, LongAdder> templateUseCounter = new HashMap<>();

        // Shuffle the triplets
        Collections.shuffle(tripletDtos);

        // Create a list to store the generated questions
        List<QuestionTestDTO> questions = new ArrayList<>();

        // Iterate over the triplets and generate questions
        for (TripletDto tripletDto : tripletDtos) {
            // Check if the desired number of questions has been reached
            if (questions.size() >= mathtestDTO.getAmount()) {
                break;
            }

            // Get the predicate for the current triplet
            PredicateDTO predicateDTO = predicatesByRdfValue.get(tripletDto.getPredicate());

            // Make a copy of the predicate's templates
            List<TemplateDTO> predicateTemplates = new ArrayList<>(predicateDTO.getTemplates());

            TemplateDTO templateDTO = null;

            // Select a template for the current triplet
            while (!predicateTemplates.isEmpty() && templateDTO == null) {
                TemplateDTO temp = getAndRemoveRandomFrom(predicateTemplates);

                // Check if the template has not been used excessively
                if (templateUseCounter.get(temp).longValue() < mathtestDTO.getTreshold()) {
                    templateDTO = temp;
                }
            }

            // Substitute placeholders in the template with triplet values to form the question
            String question = substitutePlaceholders(templateDTO.getMock(),
                tripletDto.getSubject(),
                tripletDto.getObject());

            // Determine the answer based on the template configuration
            String answer = templateDTO.isSubjectAnswer() ? tripletDto.getSubject() : tripletDto.getObject();

            // Create a question DTO and add it to the list
            QuestionTestDTO questionTestDTO = new QuestionTestDTO(question, answer);
            questions.add(questionTestDTO);

            // Increment the usage count for the selected template
            templateUseCounter.computeIfAbsent(templateDTO, k -> new LongAdder()).increment();
        }

        // Check if enough questions were generated
        if (questions.size() < mathtestDTO.getAmount()) {
            throw new IllegalArgumentException("Can't retrieve enough ontology");
        }

        // Return the generated questions
        return questions;
    }


    private <T> T getAndRemoveRandomFrom(Collection<T> collection) {
        int random = RANDOM.nextInt(collection.size());
        return new ArrayList<>(collection).remove(random);
    }


    private String substitutePlaceholders(String input, String subject, String object) {
        // Create a pattern to match placeholders in the format ${placeholder}
        Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(input);

        // Replace placeholders with the provided values if they exist
        StringBuilder output = new StringBuilder();
        while (matcher.find()) {
            String placeholder = matcher.group(1);

            if (placeholder.equals("subject") && subject != null) {
                matcher.appendReplacement(output, subject);
            } else if (placeholder.equals("object") && object != null) {
                matcher.appendReplacement(output, object);
            }
        }
        matcher.appendTail(output);

        return output.toString();
    }
}

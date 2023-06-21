package com.kfu.qs.service;

import com.kfu.qs.service.dto.TripletDto;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OntoMathEduService {


    @Value("${sparql.query.template}")
    private String sparqlQueryTemplate;

    @Value("${fuseki.host}")
    private String fusekiHost;

    public List<TripletDto> retrieveTripletsByObject(String objectKeyword) {
        String sparqlQuery = String.format(sparqlQueryTemplate, objectKeyword);

        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService(fusekiHost, query);

        ResultSet resultSet = queryExecution.execSelect();
        List<TripletDto> triplets = new ArrayList<>();

        while (resultSet.hasNext()) {
            QuerySolution solution = resultSet.nextSolution();
            String subject = solution.get("subject").toString();
            String predicate = solution.get("predicate").toString();
            String object = solution.get("object").toString();

            TripletDto triplet = new TripletDto();
            triplet.setSubject(subject);
            triplet.setPredicate(predicate);
            triplet.setObject(object);

            triplets.add(triplet);
        }

        return triplets;
    }
}

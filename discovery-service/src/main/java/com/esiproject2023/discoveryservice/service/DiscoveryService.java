package com.esiproject2023.discoveryservice.service;

import com.esiproject2023.discoveryservice.model.Content;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscoveryService {
    @Autowired
    private WebClient.Builder webClient;

    public Content[] processResponse(String params) throws JsonProcessingException {
        String[] allParams = params.split(",");
        String userDateOfBirth = allParams[0];
        String favGenre = allParams[1];
        double ratingLimit = Double.parseDouble(allParams[2]);

        String top_rated_250 = createConfig("", favGenre, "top_rated_250", "", "", "", "", "");
        String top_rated_series_250 = createConfig("", favGenre, "top_rated_series_250", "", "", "", "", "");

        String response1 = webClient.build().get().uri("http://metadata-service/metadata/searchByParams/{params}",top_rated_250).retrieve().bodyToMono(String.class).block();
        String response2 = webClient.build().get().uri("http://metadata-service/metadata/searchByParams/{params}",top_rated_series_250).retrieve().bodyToMono(String.class).block();

        Gson gson =new Gson();
        List<Content> content1 = Arrays.asList(gson.fromJson(response1, Content[].class));
        List<Content> content2 = Arrays.asList(gson.fromJson(response2, Content[].class));

        List<Content> filteredContents = content1.stream()
                .filter(content -> content.getRating() > ratingLimit).toList();
        //HERE!!! add method which checks if user >= 18 || doesn't includes Romance && doesn't include: musicVideo,podcastEpisode,podcastSeries,videoGame,video

        List<Content> contents = new ArrayList<>(content1);
        contents.addAll(content2);

        // Sort the contents by rating

        contents.sort((c1, c2) -> (int) (c2.getRating() * 100 - c1.getRating() * 100));

        // Get the top 10 contents
        Content[] top10Contents = new Content[10];
        for (int i = 0; i < 10; i++) {
            top10Contents[i] = contents.get(i);
        }
        return top10Contents;
    }


    private String createConfig(String titleType, String genre, String list, String sort, String year, String endYear, String startYear, String page) {
        LinkedHashMap<String, String> parameterTypes = new LinkedHashMap<String, String>();
        parameterTypes.put("titleType", titleType);
        parameterTypes.put("genre", genre);
        parameterTypes.put("list", list);
        parameterTypes.put("sort", sort);
        parameterTypes.put("limit", "30");
        parameterTypes.put("endYear", endYear);
        parameterTypes.put("year", year);
        parameterTypes.put("info", "custom_info");
        parameterTypes.put("page", page);
        parameterTypes.put("startYear", startYear);

        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : parameterTypes.entrySet()) {
            if (!entry.getValue().equals("")) {
                if (queryStringBuilder.length() > 0) {
                    queryStringBuilder.append("&");
                }
                queryStringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return queryStringBuilder.toString();
    }
}

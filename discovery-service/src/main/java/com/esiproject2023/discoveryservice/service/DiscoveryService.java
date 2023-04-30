package com.esiproject2023.discoveryservice.service;

import com.esiproject2023.discoveryservice.model.Content;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class DiscoveryService {

    private final WebClient webClient;

    @Autowired
    public DiscoveryService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Content[] processResponse(String params) {
        String[] allParams = params.split(",");
        String userDateOfBirth = allParams[0];
        String favGenre = allParams[1];
        String ratingLimit = allParams[2];

        String top_rated_250 = createConfig("", favGenre, "top_rated_250", "", "", "", "", "");
        String top_rated_series_250 = createConfig("", favGenre, "top_rated_series_250", "", "", "", "", "");


        String response1 = webClient.get().uri("http://localhost:8085/searchBy/" + top_rated_250).retrieve().bodyToMono(String.class).block();
        String response2 = webClient.get().uri("http://localhost:8085/searchBy/" + top_rated_series_250).retrieve().bodyToMono(String.class).block();

        System.out.println(response2);

        List<Content> content1 = new ArrayList<>();
        List<Content> content2 = new ArrayList<>();

        Map[] maps1 = new Gson().fromJson(response1, Map[].class);
        Map[] maps2 = new Gson().fromJson(response2, Map[].class);

        for (Map map : maps1) {
            //FILTERS HERE (userDateOfBirth + ratingLimit + NOT FOUND REMOVED) remove: musicVideo,podcastEpisode,podcastSeries,videoGame,video
            content1.add(new Content(map));
        }

        for (Map map : maps2) {
            //FILTERS HERE (userDateOfBirth + ratingLimit + NOT FOUND REMOVED) remove: musicVideo,podcastEpisode,podcastSeries,videoGame,video
            content2.add(new Content(map));
        }

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

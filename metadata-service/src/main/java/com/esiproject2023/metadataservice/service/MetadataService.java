package com.esiproject2023.metadataservice.service;

import com.esiproject2023.metadataservice.model.Metadata;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataService {
    @Value("${app.API_URL}")
    private String API_URL;
    @Value("${app.ContentType}")
    private String contentType;
    @Value("${app.API_Key}")
    private String API_Key;
    @Value("${app.API_Host}")
    private String API_Host;

    private final KafkaTemplate<String, HashMap> kafkaTemplate;

    @Autowired
    private Gson gson;

    public String getResponseWithParams(String params) throws Exception {
        StringBuilder restUrl = new StringBuilder();
        restUrl.append(API_URL);
        if (params.contains("AKA")) {
            restUrl.append("/search/title/");
            restUrl.append(params.substring(3));

        } else if(params.equals("/utils/genres")) {
            restUrl.append(params);
        } else restUrl.append("?").append(params);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(restUrl.toString()))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getResponseWithIDs(String ids) throws Exception {
        StringBuilder params = new StringBuilder();
        params.append("/x/titles-by-ids?idsList=");
        for (String ID : ids.split(",")) {
            if (!ID.equals("")) {
                if (params.length() > 0) {
                    params.append("%2C");
                }
                params.append(ID);
            }
        }
        params.append("&info=custom_info");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + params))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getReviewByID(String ids) throws Exception {
        StringBuilder params = new StringBuilder();
        params.append("/x/titles-by-ids?idsList=");
        for (String ID : ids.split(",")) {
            if (!ID.equals("")) {
                if (params.length() > 0) {
                    params.append("%2C");
                }
                params.append(ID);
            }
        }
        params.append("&info=custom_info");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + params))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getResponse() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?info=custom_info&limit=30"))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public Metadata[] processResponse(String response, String path) {
        HashMap<String, String> obj = new HashMap<>();
        obj.put("path", path);

        if (!response.contains("API is unreachable") && !response.contains("You are not subscribed to this API.")) {
            log.info(response);
            JsonArray results = JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("results");
            List<Metadata> metadataList = new ArrayList<>();

            if (results.isEmpty() && !response.equals("") && !response.equals("{}")) {
                obj.put("response", gson.toJson(new Metadata[0]));
                kafkaTemplate.send("backupRequest", obj);
                return new Metadata[0];
            }

            for (int i = 0; i < results.size(); i++) {
                JsonObject result = results.get(i).getAsJsonObject();
                String id = result.get("id").getAsString();
                boolean titleTextExist = !result.getAsJsonObject("titleText").get("text").isJsonNull();
                String titleText = titleTextExist ? result.getAsJsonObject("titleText").get("text").getAsString() : "";
                boolean ratingCheck = result.getAsJsonObject("ratingsSummary")
                        .get("aggregateRating")
                        .isJsonNull();

                double rating = ratingCheck ? 0 : result.getAsJsonObject("ratingsSummary")
                        .get("aggregateRating")
                        .getAsDouble();

                boolean plotText = !(result.get("plot").isJsonNull()) && (!result.getAsJsonObject("plot").get("plotText").isJsonNull());
                String description = plotText
                        ?
                        result.getAsJsonObject("plot").getAsJsonObject("plotText").get("plainText").getAsString()
                        : "";

                boolean castExist = result.get("principalCast") != null && (result.getAsJsonArray("principalCast").size()) > 0;
                JsonArray casts = castExist ? result.getAsJsonArray("principalCast").get(0).getAsJsonObject().getAsJsonArray("credits") : new JsonArray();
                StringBuilder allCasts = new StringBuilder();

                for (int itr = 0; itr < casts.size(); itr++) {
                    if (itr > 0) {
                        allCasts.append(", ");
                    }
                    String name = casts.get(itr).getAsJsonObject().getAsJsonObject("name").getAsJsonObject("nameText").get("text").getAsString();
                    allCasts.append(name);
                }

                boolean genresExist = !result.get("genres").isJsonNull();
                JsonArray genres = genresExist ? result.getAsJsonObject("genres").getAsJsonArray("genres") : new JsonArray();
                StringBuilder allGenres = new StringBuilder();
                for (int itr = 0; itr < genres.size(); itr++) {
                    if (itr > 0) {
                        allGenres.append(", ");
                    }
                    allGenres.append(genres.get(itr).getAsJsonObject().get("text").getAsString());
                }

                boolean imageExist = !result.get("primaryImage").isJsonNull() && !result.getAsJsonObject("primaryImage").get("url").isJsonNull();
                String imageURL = imageExist ? result.getAsJsonObject("primaryImage").get("url").getAsString() : "not found";

                boolean dateExist = !result.get("releaseDate").isJsonNull();
                String releaseDate;
                if (dateExist) {
                    JsonObject date = result.getAsJsonObject("releaseDate");
                    releaseDate = date.get("day") + "-" + date.get("month") + "-" + date.get("year");
                } else {
                    releaseDate = "not found";
                }

                boolean directorsExist = (result.getAsJsonArray("directors").size()) > 0;
                String director = directorsExist ? result.getAsJsonArray("directors").get(0).getAsJsonObject().getAsJsonArray("credits").get(0).getAsJsonObject().getAsJsonObject("name").getAsJsonObject("nameText").get("text").getAsString() : "not found";

                Metadata metadata = new Metadata(id, titleText, rating, allCasts.toString(), description, allGenres.toString(), imageURL, releaseDate, director); //, genre, releaseDate, organization, awards.toArray(new String[0])
                metadataList.add(metadata);
            }

            Metadata[] result = metadataList.toArray(new Metadata[0]);
            obj.put("response", gson.toJson(result));
            kafkaTemplate.send("backupRequest", obj);

            return result;
        } else {
            obj.put("response", gson.toJson(new Metadata[0]));
            kafkaTemplate.send("backupRequest", obj);

            return new Metadata[0];
        }
    }

    public List<String> processGenresResponse(String response) {

        List<String> checkList = Arrays.asList("musicVideo", "podcastEpisode", "podcastSeries", "videoGame", "video");
        HashMap<String, String> obj = new HashMap<>();
        obj.put("path", "/genres");

        if (!response.contains("API is unreachable") && !response.contains("You are not subscribed to this API.")) {
            log.info(response);
            JsonArray results = JsonParser.parseString(response).getAsJsonObject().get("results").getAsJsonArray();
            List<String> genres = new ArrayList<>();

            if (results.isEmpty() && !response.equals("") && !response.equals("{}")) {
                obj.put("response", gson.toJson(new ArrayList<>()));
                kafkaTemplate.send("backupRequest", obj);
                return new ArrayList<>();
            }
            log.info("Passed null conditions: " + results);

            for (int i = 0; i < results.size(); i++) {
                if(!results.get(i).isJsonNull()) {
                    log.info("here it comes");
                    String result = results.get(i).getAsJsonPrimitive().getAsString();
                    log.info("here it comes" + result);
                    if (!checkList.contains(result)) genres.add(result);
                }
            }
            log.info("Entered loop null conditions: " + genres);

            obj.put("response", gson.toJson(genres));
            kafkaTemplate.send("backupRequest", obj);

            return genres;
        } else {
            obj.put("response", gson.toJson(new ArrayList<>()));
            kafkaTemplate.send("backupRequest", obj);

            return new ArrayList<>();
        }
    }

}

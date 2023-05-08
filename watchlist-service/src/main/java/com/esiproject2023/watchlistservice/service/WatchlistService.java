package com.esiproject2023.watchlistservice.service;

import com.esiproject2023.watchlistservice.dto.MetadataResponse;
import com.esiproject2023.watchlistservice.dto.WatchlistItemDto;
import com.esiproject2023.watchlistservice.model.WatchlistItem;
import com.esiproject2023.watchlistservice.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private WebClient.Builder webClient;
    public String addWatchlist(WatchlistItemDto watchlistItemDto) {
        WatchlistItem watchlistItem = WatchlistItem.builder()
                .userId(watchlistItemDto.getUserId())
                .contentId(watchlistItemDto.getContentId())
                .build();
        WatchlistItem watchlistItemFindExists = watchlistRepository.findByUserIdAndContentId(watchlistItem.getUserId(), watchlistItem.getContentId());
        if(watchlistItemFindExists == null) {
            watchlistRepository.save(watchlistItem);
            log.info("The user with id {} added content with id {} to the watchlist with id {}", watchlistItem.getUserId(), watchlistItem.getContentId(), watchlistItem.getId());
            return "Success";
        } else return "Fail";

//        return mapToWatchListItemDto(result);
    }

    public WatchlistItemDto mapToWatchListItemDto(WatchlistItem watchlistItem) {
        return WatchlistItemDto.builder()
                .id(watchlistItem.getId())
                .userId(watchlistItem.getUserId())
                .contentId(watchlistItem.getContentId())
                .build();
    }

    public List<MetadataResponse> getWatchListForUser(String userId) {
        List<WatchlistItem> watchlistItems = watchlistRepository.findByUserId(userId);
        StringBuilder contentIds = new StringBuilder();
        for (WatchlistItem watchlistItem : watchlistItems) {
            contentIds.append(watchlistItem.getContentId()).append(",");
        }
        if(watchlistItems.size() == 0) return new ArrayList<>();
        MetadataResponse[] result = webClient.build().get().uri("http://metadata-service/metadata/searchByIDs/{ids}", contentIds.substring(0, contentIds.length()-1)).retrieve().bodyToMono(MetadataResponse[].class).block();
        if(result != null) return List.of(result);
        else return new ArrayList<>();
//        return List.of(result);
    }

    public void removeContentFromWatchlist(String userId, String contentId) {
        WatchlistItemDto watchlistItemDto = mapToWatchListItemDto(watchlistRepository.findByUserIdAndContentId(userId, contentId));
        watchlistRepository.deleteById(watchlistItemDto.getId());
        log.info("The content has been successfully deleted from watchlist");
    }
}

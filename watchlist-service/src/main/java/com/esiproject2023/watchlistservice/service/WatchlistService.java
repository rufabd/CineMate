package com.esiproject2023.watchlistservice.service;

import com.esiproject2023.watchlistservice.dto.WatchlistItemDto;
import com.esiproject2023.watchlistservice.model.WatchlistItem;
import com.esiproject2023.watchlistservice.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;
    public WatchlistItemDto addWatchlist(WatchlistItemDto watchlistItemDto) {
        WatchlistItem watchlistItem = WatchlistItem.builder()
                .userId(watchlistItemDto.getUserId())
                .contentId(watchlistItemDto.getContentId())
                .build();
        WatchlistItem result = watchlistRepository.save(watchlistItem);
        log.info("The user with id {} added content with id {} to the watchlist with id {}", watchlistItem.getUserId(), watchlistItem.getContentId(), watchlistItem.getId());
        return mapToWatchListItemDto(result);
    }

    public WatchlistItemDto mapToWatchListItemDto(WatchlistItem watchlistItem) {
        return WatchlistItemDto.builder()
                .id(watchlistItem.getId())
                .userId(watchlistItem.getUserId())
                .contentId(watchlistItem.getContentId())
                .build();
    }

    public List<WatchlistItemDto> getWatchListForUser(Long userId) {
        List<WatchlistItem> watchlistItems = watchlistRepository.findByUserId(userId);
        return watchlistItems.stream().map(this::mapToWatchListItemDto).toList();
    }

    public void removeMovieFromWatchlist(Long userId, String contentId) {
        WatchlistItemDto watchlistItemDto = mapToWatchListItemDto(watchlistRepository.findByUserIdAndContentId(userId, contentId));
        watchlistRepository.deleteById(watchlistItemDto.getId());
        log.info("The content has been successfully deleted from watchlist");
    }
}

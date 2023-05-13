package com.esiproject2023.watchlistservice.controller;

import com.esiproject2023.watchlistservice.dto.MetadataResponse;
import com.esiproject2023.watchlistservice.dto.WatchlistItemDto;
import com.esiproject2023.watchlistservice.service.WatchlistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
@Slf4j
@RequiredArgsConstructor
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addContentToWatchlist(@RequestBody WatchlistItemDto watchlistItemDto) {
        return watchlistService.addWatchlist(watchlistItemDto);
    }

    @GetMapping("/get/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MetadataResponse> getUserWatchlist(@PathVariable String userId) {
        return watchlistService.getWatchListForUser(userId);
    }

    @DeleteMapping("/delete/{userId}/{contentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromWatchlist(@PathVariable String userId, @PathVariable String contentId, HttpServletRequest request) {
        if(watchlistService.isUserSame(request, userId)) {
            watchlistService.removeContentFromWatchlist(userId, contentId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this resource.");
        }
    }
}

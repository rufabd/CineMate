package com.esiproject2023.watchlistservice.service;

import com.esiproject2023.watchlistservice.dto.MetadataResponse;
import com.esiproject2023.watchlistservice.dto.WatchlistItemDto;
import com.esiproject2023.watchlistservice.model.WatchlistItem;
import com.esiproject2023.watchlistservice.repository.WatchlistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private WebClient.Builder webClient;

    private static final String SECRET = "4B6150645267556B58703273357638792F423F4528482B4D6251655468566D59";
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
        MetadataResponse[] result = webClient.build().get().uri("http://navigator-service/navigator/searchByIDs/{params}", contentIds.substring(0, contentIds.length()-1)).retrieve().bodyToMono(MetadataResponse[].class).block();
        if(result != null) return List.of(result);
        else return new ArrayList<>();
//        return List.of(result);
    }

    public void removeContentFromWatchlist(String userId, String contentId) {
        WatchlistItemDto watchlistItemDto = mapToWatchListItemDto(watchlistRepository.findByUserIdAndContentId(userId, contentId));
        watchlistRepository.deleteById(watchlistItemDto.getId());
        log.info("The content has been successfully deleted from watchlist");
    }

    public boolean isUserSame(HttpServletRequest request, String userId) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")) token = authHeader.substring(7);
        return extractUsername(token).equals(userId);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key signingKey() {
        byte[] keyDecoder = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyDecoder);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}

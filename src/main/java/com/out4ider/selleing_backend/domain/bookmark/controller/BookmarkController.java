package com.out4ider.selleing_backend.domain.bookmark.controller;

import com.out4ider.selleing_backend.domain.bookmark.service.BookmarkService;
import com.out4ider.selleing_backend.global.common.dto.ResponseDto;
import com.out4ider.selleing_backend.global.security.SimpleCustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/{novelId}")
    public ResponseEntity<?> bookmarkNovel(@PathVariable(name = "novelId") Long novelId, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails){
        bookmarkService.bookmark(novelId,simpleCustomUserDetails.getUserId());
        return ResponseDto.onSuccess();
    }

    @DeleteMapping("/{novelId}")
    public ResponseEntity<?> unBookmarkNovel(@PathVariable(name = "novelId") Long novelId, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails){
        bookmarkService.unBookmark(novelId,simpleCustomUserDetails.getUserId());
        return ResponseDto.onSuccess();
    }
}

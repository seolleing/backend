package com.out4ider.selleing_backend.domain.novel.controller;

import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelTotalResponseDto;
import com.out4ider.selleing_backend.domain.novel.service.NovelService;
import com.out4ider.selleing_backend.global.common.dto.ResponseDto;
import com.out4ider.selleing_backend.global.security.SimpleCustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels")
@Slf4j
public class NovelController {
    private final NovelService novelService;

    @PostMapping
    public ResponseEntity<?> saveNovel(@RequestBody NovelRequestDto novelRequestDto) {
        Long novelId = novelService.saveNovel(novelRequestDto);
        return ResponseDto.onSuccess(novelId);
    }

    @GetMapping
    public ResponseEntity<?> getNovels(
            @RequestParam(name = "lastId", required = false) Long lastId) {
        List<NovelResponseDto> novelResponseDtos = novelService.findNovelsByLatest(lastId);
        return ResponseDto.onSuccess(novelResponseDtos);
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getNovels(
            @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "likeCount", required = false) Integer likeCount) {
        List<NovelResponseDto> novelResponseDtos = novelService.findNovelsByPopular(lastId, likeCount);
        return ResponseDto.onSuccess(novelResponseDtos);
    }

    @GetMapping("/{novelId}")
    public ResponseEntity<?> getNovel(@PathVariable(name = "novelId") Long novelId,
                                      @AuthenticationPrincipal SimpleCustomUserDetails userDetails) {
        NovelTotalResponseDto novelTotalResponseDtos = novelService.findNovel(novelId, userDetails.getUserId());
        return ResponseDto.onSuccess(novelTotalResponseDtos);
    }

    @PatchMapping("/{novelId}")
    public ResponseEntity<?> report(@PathVariable(name = "novelId") Long novelId) {
        novelService.updateReport(novelId);
        return ResponseDto.onSuccess();
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<?> getBookmarks(@RequestParam(name = "lastId", required = false) Long lastId,
                                          @AuthenticationPrincipal SimpleCustomUserDetails userDetails) {
        List<NovelResponseDto> novelResponseDtos = novelService.findBookmarksByLatest(lastId, userDetails.getUserId());
        return ResponseDto.onSuccess(novelResponseDtos);
    }
}

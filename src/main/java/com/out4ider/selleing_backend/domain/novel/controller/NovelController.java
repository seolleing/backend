package com.out4ider.selleing_backend.domain.novel.controller;

import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelTotalResponseDto;
import com.out4ider.selleing_backend.domain.novel.service.NovelService;
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
    public ResponseEntity<?> saveNovel(@RequestBody NovelRequestDto novelRequestDto){
        Long novelId = novelService.save(novelRequestDto);
        return ResponseEntity.ok().body(novelId);
    }

    @GetMapping
    public ResponseEntity<?> getNovels(@RequestParam(name="page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "orderby", required = false, defaultValue = "novelId") String orderby){
        List<NovelResponseDto> novelResponseDtos = novelService.getSome(page, orderby);
        return ResponseEntity.ok().body(novelResponseDtos);
    }

    @GetMapping("/{novelId}")
    public ResponseEntity<?> getNovel(@PathVariable(name = "novelId") Long novelId, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails){
        NovelTotalResponseDto novelTotalResponseDtos = novelService.get(novelId, simpleCustomUserDetails.getUserId());
        return ResponseEntity.ok().body(novelTotalResponseDtos);
    }

    @PatchMapping("/{novelId}")
    public ResponseEntity<?> report(@PathVariable(name="novelId") Long novelId){
        novelService.updateReport(novelId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<?> getBookmarks(@RequestParam(name="page", required = false, defaultValue = "0") int page, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails){
        List<NovelResponseDto> novelResponseDtos = novelService.getBookmarks(page, simpleCustomUserDetails.getUserId());
        return ResponseEntity.ok().body(novelResponseDtos);
    }
}

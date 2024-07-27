package com.out4ider.selleing_backend.domain.novel.controller;

import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels")
public class NovelController {
    private final NovelService novelService;

    @PostMapping
    public ResponseEntity<?> saveNovel(@RequestBody NovelRequestDto novelRequestDto){
        novelService.save(novelRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getNovels(@RequestParam(name="page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "orderby", required = false, defaultValue = "novelId") String orderby){
        List<NovelResponseDto> novelResponseDtos = novelService.getSome(page, orderby);
        return ResponseEntity.ok().body(novelResponseDtos);
    }

    @GetMapping("/{novelId}")
    public ResponseEntity<?> getNovel(@PathVariable(name = "novelId") Long novelId){
        List<NovelInfoResponseDto> novelInfoResponseDtos = novelService.get(novelId);
        return ResponseEntity.ok().body(novelInfoResponseDtos);
    }

    @PatchMapping("/{novelId}")
    public ResponseEntity<?> report(@PathVariable(name="novelId") Long novelId){
        novelService.updateReport(novelId);
        return ResponseEntity.ok().build();
    }
}

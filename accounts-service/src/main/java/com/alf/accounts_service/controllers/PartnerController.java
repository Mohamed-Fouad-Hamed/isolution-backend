package com.alf.accounts_service.controllers;

import com.alf.accounts_service.dtos.partner.*;
import com.alf.accounts_service.services.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService service;

    @PostMapping
    public ResponseEntity<PartnerResponseDto> create(
            @RequestBody PartnerCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PartnerSummaryDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> update(
            @PathVariable Long id,
            @RequestBody PartnerUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public CursorPageResponse<PartnerLookupDto> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size
    ) {

        PartnerCursor cursor =
                (lastName == null && lastId == null)
                        ? null
                        : new PartnerCursor(lastName, lastId);

        return service.search(keyword, cursor, size);
    }
}

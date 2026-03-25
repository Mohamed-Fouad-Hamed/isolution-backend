package com.alf.accounts_service.controllers;

import com.alf.accounts_service.dtos.Industry.CreateIndustryCommand;
import com.alf.accounts_service.dtos.Industry.IndustryDto;
import com.alf.accounts_service.dtos.Industry.UpdateIndustryCommand;
import com.alf.accounts_service.dtos.partner.CursorPageResponse;
import com.alf.accounts_service.dtos.partner.PartnerCursor;
import com.alf.accounts_service.dtos.partner.PartnerLookupDto;
import com.alf.accounts_service.dtos.type.CursorPageTypeResponse;
import com.alf.accounts_service.dtos.type.TypeCursor;
import com.alf.accounts_service.dtos.type.TypeLookupDto;
import com.alf.accounts_service.services.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/industries")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService service;

    @PostMapping
    public IndustryDto create(@RequestBody CreateIndustryCommand cmd) {
        return service.create(cmd);
    }

    @PutMapping("/{id}")
    public IndustryDto update(@PathVariable Long id,
                              @RequestBody UpdateIndustryCommand cmd) {
        return service.update(id, cmd);
    }

    @GetMapping("/{id}")
    public IndustryDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/tree")
    public List<IndustryDto> tree() {
        return service.getTree();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public CursorPageTypeResponse<TypeLookupDto> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size
    ) {

        TypeCursor cursor =
                (lastName == null && lastId == null)
                        ? null
                        : new TypeCursor(lastName, lastId);

        return service.search(keyword, cursor, size);
    }
}

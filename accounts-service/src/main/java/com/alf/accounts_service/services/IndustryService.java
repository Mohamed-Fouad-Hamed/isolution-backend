package com.alf.accounts_service.services;

import com.alf.accounts_service.dtos.Industry.CreateIndustryCommand;
import com.alf.accounts_service.dtos.Industry.IndustryDto;
import com.alf.accounts_service.dtos.Industry.UpdateIndustryCommand;
import com.alf.accounts_service.dtos.type.CursorPageTypeResponse;
import com.alf.accounts_service.dtos.type.TypeCursor;
import com.alf.accounts_service.dtos.type.TypeLookupDto;
import com.alf.accounts_service.mappers.IndustryMapper;
import com.alf.accounts_service.models.Industry;
import com.alf.accounts_service.repositories.IndustryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IndustryService {

    private final IndustryRepository repository;

    public IndustryDto create(CreateIndustryCommand cmd) {

        if (repository.existsByCode(cmd.code())) {
            throw new IllegalArgumentException("Industry code already exists");
        }

        Industry parent = null;
        if (cmd.parentId() != null) {
            parent = repository.findById(cmd.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
        }

        Industry industry = Industry.builder()
                .code(cmd.code())
                .name(cmd.name())
                .description(cmd.description())
                .parent(parent)
                .build();

        return IndustryMapper.toDto(repository.save(industry));
    }

    public IndustryDto update(Long id, UpdateIndustryCommand cmd) {
        Industry industry = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Industry not found"));

        industry.setName(cmd.name());
        industry.setDescription(cmd.description());
        industry.setActive(cmd.active());

        return IndustryMapper.toDto(industry);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public IndustryDto get(Long id) {
        Industry industry = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Industry not found"));

        return IndustryMapper.toDto(industry);
    }

    @Transactional(readOnly = true)
    public List<IndustryDto> getTree() {
        return repository.findByParentIsNull()
                .stream()
                .map(IndustryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CursorPageTypeResponse<TypeLookupDto> search(
            String keyword,
            TypeCursor cursor,
            int size
    ) {


        Pageable pageable = PageRequest.of(0, size);

        List<Industry> industryList = repository.searchAfter(
                keyword,
                cursor != null ? cursor.getLastSearchText() : null,
                cursor != null ? cursor.getLastId() : null,
                pageable
        );

        List<TypeLookupDto> data = industryList.stream()
                .map(p -> new TypeLookupDto(
                        Long.getLong(p.getId().toString()),
                        p.getName(),
                        p.getParent().getName()
                ))
                .toList();

        TypeCursor nextCursor = null;
        boolean hasNext = false;

        if (!industryList.isEmpty()) {
            Industry last = industryList.get(industryList.size() - 1);

            nextCursor = new TypeCursor(
                    last.getName(),
                    Long.getLong(last.getId().toString())
            );

            hasNext = industryList.size() == size;
        }

        return new CursorPageTypeResponse<>(data, nextCursor, hasNext);
    }

}

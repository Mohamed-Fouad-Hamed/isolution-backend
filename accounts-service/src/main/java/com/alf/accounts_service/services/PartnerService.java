package com.alf.accounts_service.services;

import com.alf.accounts_service.dtos.partner.*;
import com.alf.accounts_service.mappers.PartnerMapper;
import com.alf.accounts_service.models.core.Partner;
import com.alf.accounts_service.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerService {

        private final PartnerRepository partnerRepo;
        private final PartnerMapper mapper;

        /* ================= CREATE ================= */

        public PartnerResponseDto create(PartnerCreateDto dto) {

            Partner entity = mapper.toEntity(dto);

            // Serial auto generation
            entity.setSerialId(generateSerial());

            // Parent handling
            if (dto.getParentId() != null) {
                Partner parent = partnerRepo.findById(dto.getParentId())
                        .orElseThrow(() -> new RuntimeException("Parent not found"));
                entity.setParent(parent);
                entity.setCommercialPartner(parent.getCommercialPartner() != null
                        ? parent.getCommercialPartner()
                        : parent);
            } else {
                entity.setCommercialPartner(entity);
            }

            partnerRepo.save(entity);

            return mapper.toDto(entity);
        }

        /* ================= READ ================= */

        @Transactional(readOnly = true)
        public PartnerResponseDto getById(Long id) {
            Partner partner = partnerRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Partner not found"));
            return mapper.toDto(partner);
        }

        @Transactional(readOnly = true)
        public List<PartnerSummaryDto> getAll() {
            return partnerRepo.findAll()
                    .stream()
                    .map(p -> new PartnerSummaryDto(
                            p.getId(),
                            p.getName(),
                            p.getSerialId(),
                            p.getIsCompany(),
                            p.getCustomerRank(),
                            p.getSupplierRank()
                    ))
                    .toList();
        }

        /* ================= UPDATE ================= */

        public PartnerResponseDto update(Long id, PartnerUpdateDto dto) {

            Partner entity = partnerRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Partner not found"));

            mapper.updateEntity(dto, entity);

            return mapper.toDto(entity);
        }

        /* ================= DELETE ================= */

        public void delete(Long id) {
            partnerRepo.deleteById(id);
        }

        /* ================= UTIL ================= */

        private String generateSerial() {
        return "P-" + System.currentTimeMillis();
    }

        private void applyHierarchy(
                Partner p,
                Long parentId,
                Long commercialId) {

            if (parentId != null) {
                p.setParent(partnerRepo.getReferenceById(parentId));
            }

            if (commercialId != null) {
                p.setCommercialPartner(
                        partnerRepo.getReferenceById(commercialId)
                );
            } else {
                p.setCommercialPartner(p);
            }
        }


        public CursorPageResponse<PartnerLookupDto> search(
                String keyword,
                PartnerCursor cursor,
                int size
        ) {

            Pageable pageable = PageRequest.of(0, size);

            List<Partner> partners = partnerRepo.searchAfter(
                    keyword,
                    cursor != null ? cursor.getLastName() : null,
                    cursor != null ? cursor.getLastId() : null,
                    pageable
            );

            List<PartnerLookupDto> data = partners.stream()
                    .map(p -> new PartnerLookupDto(
                            p.getId(),
                            p.getName(),
                            p.getDisplayName()
                    ))
                    .toList();

            PartnerCursor nextCursor = null;
            boolean hasNext = false;

            if (!partners.isEmpty()) {
                Partner last = partners.get(partners.size() - 1);

                nextCursor = new PartnerCursor(
                        last.getName(),
                        last.getId()
                );

                hasNext = partners.size() == size;
            }

            return new CursorPageResponse<>(data, nextCursor, hasNext);
        }






}



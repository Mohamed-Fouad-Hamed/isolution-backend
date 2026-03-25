package com.alf.accounts_service.services;


import com.alf.accounts_service.components.IdPrefixConfig;
import com.alf.accounts_service.models.IdSequence;
import com.alf.accounts_service.repositories.IdSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdGeneratorService {

  private final IdSequenceRepository idSequenceRepository ;

  private final IdPrefixConfig idPrefixConfig ;

    @Transactional
    public String generateNextId(String entityName) {

        IdSequence sequence = idSequenceRepository.findByEntityNameForUpdate(entityName.toLowerCase())
                .orElseThrow(() -> new RuntimeException("No sequence found for entity: " + entityName));

        long nextNumber = sequence.getLastNumber() + 1;
        String formatStr = "%s%0" + sequence.getCodeLen() + "d";
        sequence.setLastNumber(nextNumber);
        idSequenceRepository.save(sequence);
        String prefix = idPrefixConfig.getPrefixFor(entityName);

        return String.format( formatStr, prefix, nextNumber);

    }



}

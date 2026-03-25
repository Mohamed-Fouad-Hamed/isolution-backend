package com.alf.accounts_service.services;

import com.alf.accounts_service.dtos.payment.CreatePaymentTermCommand;
import com.alf.accounts_service.dtos.payment.CreatePaymentTermLineCommand;
import com.alf.accounts_service.dtos.type.CursorPageTypeResponse;
import com.alf.accounts_service.dtos.type.TypeCursor;
import com.alf.accounts_service.dtos.type.TypeLookupDto;
import com.alf.accounts_service.models.Industry;
import com.alf.accounts_service.models.PaymentTerm;
import com.alf.accounts_service.models.PaymentTermLine;
import com.alf.accounts_service.repositories.PaymentTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentTermService {

    private final PaymentTermRepository repository;

    public PaymentTerm create(CreatePaymentTermCommand cmd){

        PaymentTerm term = new PaymentTerm();

        term.setCode(cmd.code());
        term.setName(cmd.name());
        term.setDescription(cmd.description());

        for (CreatePaymentTermLineCommand l : cmd.lines()) {

            PaymentTermLine line = new PaymentTermLine();

            line.setValueType(l.valueType());
            line.setValueAmount(l.valueAmount());
            line.setDueType(l.dueType());
            line.setDays(l.days());
            line.setSequence(l.sequence());

            term.addLine(line);
        }

        return repository.save(term);
    }

    public List<PaymentTerm> list(){
        return repository.findAll();
    }

    public PaymentTerm get(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentTerm not found"));
    }


    @Transactional(readOnly = true)
    public CursorPageTypeResponse<TypeLookupDto> search(
            String keyword,
            TypeCursor cursor,
            int size
    ) {


        Pageable pageable = PageRequest.of(0, size);

        List<PaymentTerm> paymentTermList = repository.searchAfter(
                keyword,
                cursor != null ? cursor.getLastSearchText() : null,
                cursor != null ? cursor.getLastId() : null,
                pageable
        );

        List<TypeLookupDto> data = paymentTermList.stream()
                .map(p -> new TypeLookupDto(
                        Long.getLong(p.getId().toString()),
                        p.getName(),
                        p.getName()
                ))
                .toList();

        TypeCursor nextCursor = null;
        boolean hasNext = false;

        if (!paymentTermList.isEmpty()) {
            PaymentTerm last = paymentTermList.get(paymentTermList.size() - 1);

            nextCursor = new TypeCursor(
                    last.getName(),
                    Long.getLong(last.getId().toString())
            );

            hasNext = paymentTermList.size() == size;
        }

        return new CursorPageTypeResponse<>(data, nextCursor, hasNext);
    }


}


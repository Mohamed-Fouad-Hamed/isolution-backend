package com.alf.inventory.services;

import com.alf.inventory.entity.Uom;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class UomConversionService {


    public BigDecimal convert(BigDecimal qty,
                              Uom from,
                              Uom to) {

        validateSameGroup(from, to);

        // 1️⃣ تحويل إلى Base
        BigDecimal baseQty = qty.multiply(from.getFactor());

        // 2️⃣ تحويل من Base إلى Target
        BigDecimal result = baseQty.divide(
                to.getFactor(),
                10,
                RoundingMode.HALF_UP
        );

        // 3️⃣ تطبيق rounding
        return applyRounding(result, to);
    }

    private void validateSameGroup(Uom from, Uom to) {

        if (!from.getGroup().getId().equals(to.getGroup().getId())) {
            throw new RuntimeException("Different UOM Groups not allowed");
        }
    }

    private BigDecimal applyRounding(BigDecimal qty, Uom uom) {

        BigDecimal rounding = uom.getRounding();

        if (rounding == null || rounding.compareTo(BigDecimal.ZERO) == 0) {
            return qty;
        }

        return qty.divide(rounding, 0, RoundingMode.HALF_UP)
                .multiply(rounding);
    }

    public BigDecimal convertPrice(BigDecimal price,
                                   Uom from,
                                   Uom to) {

        validateSameGroup(from, to);

        BigDecimal basePrice = price.divide(from.getFactor(), 10, RoundingMode.HALF_UP);

        return basePrice.multiply(to.getFactor());
    }


    // Example, usage convert while all transactions
   /* BigDecimal baseQty = uomConversionService.convert(
            inputQty,
            inputUom,
            product.getUom() // Base UOM
    );*/







}

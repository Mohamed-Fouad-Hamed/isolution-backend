package com.alf.accounts_service.components;

import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class IdPrefixConfig {
    private final Map<String, String> prefixMap = Map.ofEntries(
            Map.entry("app_accounts", "AC"),
            Map.entry("bills", "BI"),
            Map.entry("invoices", "IN"),
            Map.entry("cost_center", "CC"),
            Map.entry("currency", "CU"),
            Map.entry("department", "DE"),
            Map.entry("exchange_rate", "ER"),
            Map.entry("fin_payments", "FP"),
            Map.entry("financial_account", "FC"),
            Map.entry("financial_account_type", "FT"),
            Map.entry("fiscal_period", "FSP"),
            Map.entry("fiscal_year", "FY"),
            Map.entry("journal_entry", "JE"),
            Map.entry("journals", "JO"),
            Map.entry("tax", "TX"),
            Map.entry("tax_type", "TT"),
            Map.entry("partner", "PA")
    );

    public String getPrefixFor(String entityName) {
        return prefixMap.getOrDefault(entityName, "XX");
    }
}

package com.alf.inventory.dto;

import java.util.List;

public class ProductDetailsResponse {
    private Long productId;
    private String name;
    private Double price;

    private List<MediaResourceDTO> media;
}

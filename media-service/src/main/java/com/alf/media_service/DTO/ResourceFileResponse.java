package com.alf.media_service.DTO;

public record ResourceFileResponse (
        String id,
        String url,
        String thumbnailUrl,
        String fileName,
        String contentType,
        long size
) {

}

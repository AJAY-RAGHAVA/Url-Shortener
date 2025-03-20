package com.example.urlshortener.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OriginalUrlRequest {
    private String originalUrl;
    private int senderAccountNumber;
    private String cdsid;
}

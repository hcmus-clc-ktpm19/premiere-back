package org.hcmus.premiere.model.dto;

import java.io.Serializable;

public record ErrorDto(String message, String i18nPlaceHolder) implements Serializable {
}

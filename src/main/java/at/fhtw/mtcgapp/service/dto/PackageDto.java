package at.fhtw.mtcgapp.service.dto;

import at.fhtw.mtcgapp.model.Package;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@Builder
public record PackageDto(UUID id, int price, List<CardDto> cards) {
    public PackageDto(Package _package) {
        this(_package.getToken(), _package.getPrice(), _package.getCards().stream().map(CardDto::new).toList());
        log.debug("Created PackageDto {}", this);
    }
}

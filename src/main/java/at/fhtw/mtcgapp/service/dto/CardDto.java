package at.fhtw.mtcgapp.service.dto;

import at.fhtw.mtcgapp.model.Card;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Builder
public record CardDto(UUID id, String name, double damage, String damageType) {
    public CardDto(Card card) {
        this(card.getToken(), card.getName(), card.getDamage(), card.getDamageType().getDbValue());
        log.debug("Created CardDto {}", this);
    }
}

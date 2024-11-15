package at.fhtw.mtcgapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trade {
    private UUID id;
    private double minimumDamage;

    private Card cardToTrade;
    private User trader;
}

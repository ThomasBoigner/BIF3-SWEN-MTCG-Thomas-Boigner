package at.fhtw.mtcgapp.model;

import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trade {
    private long id;
    private UUID token;
    private double minimumDamage;

    private CardType type;
    private Card cardToTrade;
    private User trader;

    @Override
    public String toString() {
        return "Trade{" +
               "minimumDamage=" + minimumDamage +
               ", token=" + token +
               ", id=" + id +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return id == trade.id && Double.compare(minimumDamage, trade.minimumDamage) == 0 && Objects.equals(token, trade.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, minimumDamage);
    }
}

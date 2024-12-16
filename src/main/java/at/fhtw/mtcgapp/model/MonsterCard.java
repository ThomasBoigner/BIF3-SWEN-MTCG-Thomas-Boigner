package at.fhtw.mtcgapp.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MonsterCard extends Card {
    private double defence;

    @Override
    public String toString() {
        return "MonsterCard{" +
               "defence=" + defence +
               "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MonsterCard that = (MonsterCard) o;
        return Double.compare(defence, that.defence) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), defence);
    }
}

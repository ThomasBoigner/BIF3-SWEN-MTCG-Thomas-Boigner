package at.fhtw.mtcgapp.model;

import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Package {
    private long id;
    private UUID token;
    private int price;

    private List<Card> cards;

    @Override
    public String toString() {
        return "Package{" +
               "id=" + id +
               ", token=" + token +
               ", price=" + price +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return id == aPackage.id && price == aPackage.price && Objects.equals(token, aPackage.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, price);
    }
}

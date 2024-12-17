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
public class User {
    private long id;
    private UUID token;
    private String username;
    private String password;
    private String bio;
    private String image;
    private int coins;
    private int elo;
    private int battlesFought;

    private List<Card> stack;
    private List<Card> deck;
    private List<Trade> trades;

    @Override
    public String toString() {
        return "User{" +
               "battlesFought=" + battlesFought +
               ", elo=" + elo +
               ", coins=" + coins +
               ", image='" + image + '\'' +
               ", bio='" + bio + '\'' +
               ", password='" + password + '\'' +
               ", username='" + username + '\'' +
               ", token=" + token +
               ", id=" + id +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && coins == user.coins && elo == user.elo && battlesFought == user.battlesFought && Objects.equals(token, user.token) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(bio, user.bio) && Objects.equals(image, user.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, username, password, bio, image, coins, elo, battlesFought);
    }
}

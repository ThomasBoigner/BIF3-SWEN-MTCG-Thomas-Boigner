package at.fhtw.mtcgapp.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {
    private long id;
    private String token;

    private User user;

    @Override
    public String toString() {
        return "Session{" +
               "token='" + token + '\'' +
               ", id=" + id +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return id == session.id && Objects.equals(token, session.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }
}

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
public class user {
    private UUID id;
    private String username;
    private String password;
    private String bio;
    private String image;
    private int coins;
    private int elo;
    private int battlesFought;
}

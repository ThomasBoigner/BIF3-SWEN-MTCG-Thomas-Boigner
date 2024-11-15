package at.fhtw.mtcgapp.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonsterCard extends Card {
    private double defence;
}

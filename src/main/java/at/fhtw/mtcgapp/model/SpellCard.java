package at.fhtw.mtcgapp.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpellCard extends Card {
    private double criticalHitChance;
}

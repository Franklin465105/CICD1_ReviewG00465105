package ie.atu.sample.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private String name;
    private int capacity;
}
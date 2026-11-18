package ie.atu.sample.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotBlank(message = "Organiser name is required")
    @Size(min = 2, max = 60, message = "Organiser name must be between 2 and 60 characters")
    private String organiserName;

    @NotNull(message = "Attendee count is required")
    @Positive(message = "Attendee count must be a positive whole number")
    private Integer attendeeCount;
}
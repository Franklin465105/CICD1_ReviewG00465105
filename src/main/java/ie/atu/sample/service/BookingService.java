package ie.atu.sample.service;

import feign.FeignException;
import ie.atu.sample.client.RoomClient;
import ie.atu.sample.dto.BookingRequest;
import ie.atu.sample.dto.BookingResponse;
import ie.atu.sample.dto.RoomDto;
import ie.atu.sample.model.Booking;
import ie.atu.sample.repository.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;

    public BookingService(BookingRepository bookingRepository, RoomClient roomClient) {
        this.bookingRepository = bookingRepository;
        this.roomClient = roomClient;
    }

    public BookingResponse createBooking(BookingRequest request) {
        RoomDto room;
        try {
            room = roomClient.getRoomById(request.getRoomId());
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Room not found");
        }

        if (request.getAttendeeCount() > room.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Attendee count exceeds room capacity");
        }

        Booking booking = Booking.builder()
                .organiserName(request.getOrganiserName())
                .attendeeCount(request.getAttendeeCount())
                .roomId(request.getRoomId())
                .status("ACTIVE")
                .build();

        return toResponse(bookingRepository.save(booking));
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));
        return toResponse(booking);
    }

    private BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .organiserName(booking.getOrganiserName())
                .attendeeCount(booking.getAttendeeCount())
                .roomId(booking.getRoomId())
                .status(booking.getStatus())
                .build();
    }
}
package ru.practicum.shareit.booking.dto;

public enum BookingStatus {
    WAITING,  // новое бронирование, ожидает одобрения
    APPROVED, // бронирование подтверждено владельцем
    REJECTED, // бронирование отклонено владельцем
    CANCELED, // бронирование отменено создателем
    CURRENT,  // текущие бронирования
    FUTURE,   // будущие бронирования
    PAST,     // завершенные бронирования
    ALL;      // все бронирования

    public static BookingStatus from(String status) {
        for (BookingStatus value : BookingStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }
}

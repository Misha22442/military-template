package ua.edu.viti.military.entity;

public enum MovementType {
    ASSIGN_DRIVER,      // Призначення водія
    UNASSIGN_DRIVER,    // Відкликання водія
    SEND_TO_MAINTENANCE,// Відправка в ТО
    RETURN_FROM_MAINTENANCE, // Повернення з ТО
    DEPARTURE,          // Вибуття на завдання
    ARRIVAL,            // Прибуття з завдання
    FUEL_REFILL,        // Заправка пальним
    STATUS_CHANGE       // Зміна статусу
}

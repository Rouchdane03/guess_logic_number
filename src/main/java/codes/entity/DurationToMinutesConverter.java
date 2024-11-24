package codes.entity;

import jakarta.persistence.AttributeConverter;

import java.time.Duration;

public class DurationToMinutesConverter  implements AttributeConverter<Duration, String> {

    // Convertir Duration en String pour la base de données
    @Override
    public String convertToDatabaseColumn(Duration duration) {
        if (duration == null) return null;
        long minutes = duration.toMinutesPart(); // Partie minute
        int seconds = duration.toSecondsPart(); // Partie seconde
        return String.format("%02dmin:%02ds", minutes, seconds);
    }

    // Convertir String en Duration pour l'entité Java
    @Override
    public Duration convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        String[] parts = dbData.split("[^0-9]+");
        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);
        return Duration.ofMinutes(minutes).plusSeconds(seconds);
    }
}

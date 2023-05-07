package esi2023.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FrequencyPreference {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    TWICE_PER_WEEK("twicePerWeek"),
    THRICE_PER_WEEK("thricePerWeek");

    private final String frequencyName;

    public FrequencyPreference fromString(String preferenceString) {
        for (var preference : FrequencyPreference.values()) {
            if (preference.getFrequencyName().equalsIgnoreCase(preferenceString)) {
                return preference;
            }
        }
        throw new IllegalArgumentException("Invalid frequency preference: " + preferenceString);
    }
}

package esi2023.project.repository;

import esi2023.project.dto.FrequencyPreference;
import esi2023.project.dto.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    List<UserProfile> findUserProfilesByFrequencyPreferenceIgnoreCase(FrequencyPreference preference);

}

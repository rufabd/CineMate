package esi2023.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiscoveryRequest {
    private String dob;
    private String genre;
    private String rating;
}

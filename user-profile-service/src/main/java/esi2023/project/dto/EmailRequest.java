package esi2023.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String user_email;
    private String title;
    private String subject;
    private String content;
    private String type;
}

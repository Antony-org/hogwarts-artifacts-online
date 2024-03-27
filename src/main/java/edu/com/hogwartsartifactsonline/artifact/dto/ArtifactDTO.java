package edu.com.hogwartsartifactsonline.artifact.dto;

import edu.com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDTO(String id,
                          @NotEmpty(message = "A name is required")
                          String name,
                          @NotEmpty(message = "A description is required")
                          String description,
                          @NotEmpty(message = "An imageUrl is required")
                          String imageUrl,
                          WizardDTO owner) {
    @Override
    public String id() {
        return id;
    }

}

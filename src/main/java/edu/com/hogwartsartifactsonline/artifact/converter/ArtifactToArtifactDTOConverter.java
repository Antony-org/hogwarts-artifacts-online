package edu.com.hogwartsartifactsonline.artifact.converter;

import edu.com.hogwartsartifactsonline.artifact.Artifact;
import edu.com.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import edu.com.hogwartsartifactsonline.wizard.converter.WizardToWizardDTOConverter;
import edu.com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDTOConverter implements Converter<Artifact, ArtifactDTO> {

    private final WizardToWizardDTOConverter wizardToWizardDTOConverter;

    public ArtifactToArtifactDTOConverter(WizardToWizardDTOConverter wizardToWizardDTOConverter) {
        this.wizardToWizardDTOConverter = wizardToWizardDTOConverter;
    }

    @Override
    public ArtifactDTO convert(Artifact source) {

        WizardDTO wizardDTO = source.getOwner() == null ? null
                : this.wizardToWizardDTOConverter.convert(source.getOwner());

        ArtifactDTO artifactDTO = new ArtifactDTO(source.getId(), source.getName(), source.getDescription(),
                source.getImageUrl(), wizardDTO);

        return artifactDTO;
    }
}

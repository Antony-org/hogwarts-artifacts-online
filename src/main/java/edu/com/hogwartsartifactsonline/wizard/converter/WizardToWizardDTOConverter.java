package edu.com.hogwartsartifactsonline.wizard.converter;

import edu.com.hogwartsartifactsonline.wizard.Wizard;
import edu.com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDTOConverter implements Converter<Wizard, WizardDTO> {
    @Override
    public WizardDTO convert(Wizard source) {
        WizardDTO wizardDTO = new WizardDTO(source.getId(), source.getName(), source.getNumberOfArtifacts());
        return wizardDTO;
    }
}

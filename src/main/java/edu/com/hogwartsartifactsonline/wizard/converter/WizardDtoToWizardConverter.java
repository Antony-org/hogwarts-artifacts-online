package edu.com.hogwartsartifactsonline.wizard.converter;

import edu.com.hogwartsartifactsonline.wizard.Wizard;
import edu.com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDTO, Wizard> {
    @Override
    public Wizard convert(WizardDTO source) {
        Wizard wizard = new Wizard();
        wizard.setId(source.id());
        wizard.setName(source.name());

        return wizard;
    }
}

package edu.com.hogwartsartifactsonline.wizard;

import edu.com.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    private final IdWorker idWorker;

    public WizardService(WizardRepository wizardRepository, IdWorker idWorker) {
        this.wizardRepository = wizardRepository;
        this.idWorker = idWorker;
    }

    public Wizard findById(Integer wizardId){
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", wizardId));
    }

    public List<Wizard> findAll(){
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard wizard){
        return this.wizardRepository.save(wizard);
    }

    public Wizard update(Integer wizardId, Wizard wizard){
        Wizard foundWizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", wizardId));
        foundWizard.setName(wizard.getName());

        return this.wizardRepository.save(foundWizard);
    }

    public void delete(Integer wizardId){
        Wizard foundWizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", wizardId));

        foundWizard.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }
}

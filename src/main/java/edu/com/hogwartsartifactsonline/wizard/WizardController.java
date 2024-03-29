package edu.com.hogwartsartifactsonline.wizard;

import edu.com.hogwartsartifactsonline.system.Result;
import edu.com.hogwartsartifactsonline.system.StatusCode;
import edu.com.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import edu.com.hogwartsartifactsonline.wizard.converter.WizardToWizardDTOConverter;
import edu.com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    WizardService wizardService;

    WizardToWizardDTOConverter wizardToWizardDTOConverter;

    WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService,
                            WizardToWizardDTOConverter wizardToWizardDTOConverter,
                            WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDTOConverter = wizardToWizardDTOConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId){
        Wizard wizard = this.wizardService.findById(wizardId);
        WizardDTO wizardDTO = wizardToWizardDTOConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDTO);
    }

    @GetMapping
    public Result findAllWizards(){
        List<Wizard> wizardList = this.wizardService.findAll();

        List<WizardDTO> wizardDTOList = wizardList.stream().
                map(foundWizard -> this.wizardToWizardDTOConverter.convert(foundWizard))
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDTOList);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDTO wizardDTO){
        //Convert WizardDto to Wizard to pass to wizardService
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDTO);
        Wizard savedWizard = this.wizardService.save(wizard);

        //Convert Wizard to WizardDto to parse as Json
        WizardDTO savedWizardDTO = this.wizardToWizardDTOConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedWizardDTO);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDTO wizardDTO){
        //Convert WizardDto to Wizard to pass to wizardService
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDTO);

        Wizard updatedWizard = this.wizardService.update(wizardId, wizard);

        //Convert Wizard to WizardDto to parse as Json
        WizardDTO updatedWizardDTO = this.wizardToWizardDTOConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedWizardDTO);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId){
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }
}

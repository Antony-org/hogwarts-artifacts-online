package edu.com.hogwartsartifactsonline.artifact;

import edu.com.hogwartsartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import edu.com.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDTOConverter;
import edu.com.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import edu.com.hogwartsartifactsonline.system.Result;
import edu.com.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    private final ArtifactToArtifactDTOConverter artifactToArtifactDTOConverter;

    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDTOConverter artifactToArtifactDTOConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDTOConverter = artifactToArtifactDTOConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDTO artifactDTO = this.artifactToArtifactDTOConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDTO);
    }

    @GetMapping
    public Result findALl(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();

        // convert found artifacts to a list of artifactDTOs
        List<ArtifactDTO> artifactDTOs = foundArtifacts.stream().
                map(foundArtifact -> this.artifactToArtifactDTOConverter.convert(foundArtifact)).
                collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactDTOs);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDTO artifactDTO){
        // Convert ArtifactDTO to Artifact
        Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDTO);

        Artifact savedArtifact = this.artifactService.save(newArtifact);
        ArtifactDTO savedArtifactDTO = this.artifactToArtifactDTOConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifactDTO);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDTO artifactDTO){
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDTO);

        Artifact updatedArtifact = this.artifactService.update(artifactId, update);
        ArtifactDTO updatedArtifactDto = this.artifactToArtifactDTOConverter.convert(updatedArtifact);

        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifactDto);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.deleteArtifact(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

}

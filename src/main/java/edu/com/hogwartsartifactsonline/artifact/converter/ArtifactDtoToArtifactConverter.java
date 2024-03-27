package edu.com.hogwartsartifactsonline.artifact.converter;

import edu.com.hogwartsartifactsonline.artifact.Artifact;
import edu.com.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDTO, Artifact> {

    @Override
    public Artifact convert(ArtifactDTO source) {

        Artifact artifact = new Artifact();
        artifact.setId(source.id());
        artifact.setName(source.name());
        artifact.setDescription(source.description());
        artifact.setImageUrl(source.imageUrl());

        return artifact;
    }
}

package edu.com.hogwartsartifactsonline.artifact;

import edu.com.hogwartsartifactsonline.artifact.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    private List<Artifact> artifacts = new ArrayList<>();

    public Artifact findById(String artifactId){
        return this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }
    public List<Artifact> findAll(){
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update){
        return this.artifactRepository.findById(artifactId)
                .map(oldArtifact ->{
                    oldArtifact.setName(update.getName());
                    oldArtifact.setDescription(update.getDescription());
                    oldArtifact.setImageUrl(update.getImageUrl());
                    return this.artifactRepository.save(oldArtifact);
                })
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public void deleteArtifact(String artifactId){
         Artifact artifact = this.artifactRepository.findById(artifactId)
                 .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
         this.artifactRepository.deleteById(artifactId);
    }

}

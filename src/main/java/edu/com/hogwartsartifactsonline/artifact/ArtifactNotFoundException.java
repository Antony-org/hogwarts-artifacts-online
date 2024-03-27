package edu.com.hogwartsartifactsonline.artifact;

public class ArtifactNotFoundException extends RuntimeException{

    public ArtifactNotFoundException(String id){
        super("Could not find an artifact with ID " + id);
    }

}

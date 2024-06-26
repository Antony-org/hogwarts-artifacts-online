package edu.com.hogwartsartifactsonline.artifact;

import edu.com.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.com.hogwartsartifactsonline.wizard.Wizard;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        this.artifacts = new ArrayList<>();
        artifacts.add(a1);
        artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object artifactRepository
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl"
        */
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);

        // Defines the behaviour of Mock object
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));

        //When. Act on the target behaviour. Cover the methods to be tested
        Artifact foundArtifact = artifactService.findById("1250808601744904192");

        //Then. Assert expected outcomes
        assertThat(foundArtifact.getId()).isEqualTo(a.getId());
        assertThat(foundArtifact.getName()).isEqualTo(a.getName());
        assertThat(foundArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(foundArtifact.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(artifactRepository, times(1)).findById("1250808601744904192");

    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            Artifact foundArtifact = artifactService.findById("1250808601744904192");
        });

        // Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).
                hasMessage("Could not find artifact with Id 1250808601744904192");

        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess(){
        // Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        // When
        List<Artifact> foundArtifacts = artifactService.findAll();

        // Then
        assertThat(foundArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testCreateArtifactSuccess(){
        // Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 7");
        newArtifact.setDescription("desc");
        newArtifact.setImageUrl("ImageUrl");

        given(this.idWorker.nextId()).willReturn(123456L);
        given(this.artifactRepository.save(newArtifact)).willReturn(newArtifact);

        // When
        Artifact createdArtifact = artifactService.save(newArtifact);

        // Then
        assertThat(createdArtifact.getId()).isEqualTo("123456");
        assertThat(createdArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(createdArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(createdArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess(){
        //Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("desc");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        //update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("new desc");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        //When
        Artifact updatedArtifact = artifactService.update("1250808601744904192", update);

        //Then
        assertThat(updatedArtifact.getId()).isEqualTo("1250808601744904192");
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times(1)).save(oldArtifact);
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Artifact update = new Artifact();
        update.setName("Invisibility Cloak");
        update.setDescription("new desc");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, ()-> {
            artifactService.update("1250808601744904192", update);
        });

        //Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess(){
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");
        // Defines the behaviour of Mock object
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        doNothing().when(artifactRepository).deleteById("1250808601744904192");

        //When. Act on the target behaviour. Cover the methods to be tested
        artifactService.deleteArtifact("1250808601744904192");

        //Then. Assert expected outcomes
        verify(artifactRepository, times(1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteNotFound(){
        //Given

        // Defines the behaviour of Mock object
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //When. Act on the target behaviour. Cover the methods to be tested
        assertThrows(ObjectNotFoundException.class, ()-> {
            artifactService.deleteArtifact("1250808601744904192");
        });
        //Then. Assert expected outcomes
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

}
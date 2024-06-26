package edu.com.hogwartsartifactsonline.wizard;

import edu.com.hogwartsartifactsonline.artifact.Artifact;
import edu.com.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.com.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards = new ArrayList<>();

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

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);

        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object WizardRepository
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904191");
        a2.setName("Deluminator");
        a2.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a2.setImageUrl("ImageUrl");


        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Harry Potter");
        w.addArtifact(a);
        w.addArtifact(a2);

        given(wizardRepository.findById(1)).willReturn(Optional.of(w));

        //When
        Wizard foundWizard = wizardService.findById(1);

        //Then
        assertThat(foundWizard.getId()).isEqualTo(w.getId());
        assertThat(foundWizard.getName()).isEqualTo(w.getName());
        assertThat(foundWizard.getNumberOfArtifacts()).isEqualTo(w.getNumberOfArtifacts());

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            Wizard foundWizard = wizardService.findById(1);
        });

        // Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).
                hasMessage("Could not find Wizard with Id " + 1);

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindAllSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object WizardRepository
        given(wizardRepository.findAll()).willReturn(this.wizards);

        //When
        List<Wizard> foundWizards = wizardService.findAll();

        //Then
        assertThat(foundWizards.get(0).getId()).isEqualTo(this.wizards.get(0).getId());
        assertThat(foundWizards.get(0).getName()).isEqualTo(this.wizards.get(0).getName());
        assertThat(foundWizards.size()).isEqualTo(this.wizards.size());

        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testSaveWizardSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object WizardRepository
        Wizard w = new Wizard();
        w.setName("blabla");

        given(wizardRepository.save(w)).willReturn(w);

        //When
        Wizard savedWizard = this.wizardService.save(w);

        //Then
        assertThat(savedWizard.getName()).isEqualTo(w.getName());

        verify(wizardRepository, times(1)).save(w);
    }

    @Test
    void testUpdateSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object WizardRepository
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Harry Potter");

        given(wizardRepository.findById(1)).willReturn(Optional.of(w));
        given(wizardRepository.save(w)).willReturn(w);

        //When
        Wizard foundWizard = wizardService.update(1, w);

        //Then
        assertThat(foundWizard.getId()).isEqualTo(w.getId());
        assertThat(foundWizard.getName()).isEqualTo(w.getName());
        assertThat(foundWizard.getNumberOfArtifacts()).isEqualTo(w.getNumberOfArtifacts());

        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(w);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Wizard w = new Wizard();
        w.setName("Harry Potter");

        given(this.wizardRepository.findById(4)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, ()-> {
            this.wizardService.update(4, w);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(4);
    }

    @Test
    void testDeleteSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object WizardRepository
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Harry Potter");

        given(wizardRepository.findById(1)).willReturn(Optional.of(w));
        doNothing().when(this.wizardRepository).deleteById(1);

        //When
        wizardService.delete(1);

        //Then
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
        //Given

        given(this.wizardRepository.findById(1)).willThrow(new ObjectNotFoundException("wizard", 1));

        //When
        assertThrows(ObjectNotFoundException.class, ()-> {
            this.wizardService.delete(1);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testAssignArtifactSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object WizardRepository
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904191");
        artifact.setName("Deluminator");
        artifact.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        artifact.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Harry Potter");
        w.addArtifact(artifact);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Neville Longbottom");

        given(this.wizardRepository.findById(2)).willReturn(Optional.of(w2));
        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(artifact));

        //When
        this.wizardService.assignArtifact(2, "1250808601744904191");

        //Then
        assertThat(artifact.getOwner().getId()).isEqualTo(2);
        assertThat(w2.getArtifacts()).contains(artifact);
        assertThat(w.getNumberOfArtifacts()).isEqualTo(0);
        assertThat(w2.getNumberOfArtifacts()).isEqualTo(1);

        verify(this.wizardRepository, times(1)).findById(2);
        verify(this.artifactRepository, times(1)).findById("1250808601744904191");
    }

    @Test
    void testAssignArtifactErrorWizardNotFound(){
        //Given
        Wizard w = new Wizard();
        w.setName("Harry Potter");
        w.setId(1);

        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904191");
        artifact.setName("Deluminator");
        artifact.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        artifact.setImageUrl("ImageUrl");

        w.addArtifact(artifact);

        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());
        //given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(artifact));

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> {
            this.wizardService.assignArtifact(2, "1250808601744904191");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find wizard with Id " + 2);

        assertThat(artifact.getOwner().getId()).isEqualTo(1);
        verify(this.wizardRepository, times(1)).findById(2);
    }

    @Test
    void testAssignArtifactErrorArtifactNotFound(){
        //Given
        Wizard w = new Wizard();
        w.setName("Harry Potter");
        w.setId(1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Neville Longbottom");

        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(w2));

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> {
            this.wizardService.assignArtifact(2, "1250808601744904191");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id " + "1250808601744904191");

        assertThat(w.getNumberOfArtifacts()).isEqualTo(0);
        verify(this.wizardRepository, times(1)).findById(2);
    }

}
package edu.com.hogwartsartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.com.hogwartsartifactsonline.artifact.Artifact;
import edu.com.hogwartsartifactsonline.system.StatusCode;
import edu.com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizardList;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.wizardList = new ArrayList<>();

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

        this.wizardList.add(w1);
        this.wizardList.add(w2);
        this.wizardList.add(w3);
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        //Given
        given(this.wizardService.findById(1)).willReturn(this.wizardList.get(0));

        //When and Then
        this.mockMvc.perform(get(this.baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Find One Success")).
                andExpect(jsonPath("$.data.id").value(1)).
                andExpect(jsonPath("$.data.name").value(wizardList.get(0).getName()));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        //Given
        given(this.wizardService.findById(4)).willThrow(new ObjectNotFoundException("wizard", 4));
        Integer id = 4;
        //When and Then
        this.mockMvc.perform(get(this.baseUrl + "/wizards/4").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(false)).
                andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND)).
                andExpect(jsonPath("$.message").value("Could not find wizard with Id " + id)).
                andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllWizardsSuccess() throws Exception {
        //Given
        given(this.wizardService.findAll()).willReturn(this.wizardList);

        //When and Then
        this.mockMvc.perform(get(this.baseUrl + "/wizards").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Find All Success")).
                andExpect(jsonPath("$.data", Matchers.hasSize(this.wizardList.size()))).
                andExpect(jsonPath("$.data[0].name").value(wizardList.get(0).getName()));
    }

    @Test
    void testAddWizardSuccess() throws Exception {
        //Given
        WizardDTO newWizard = new WizardDTO(4,"blabla",0);

        String json = objectMapper.writeValueAsString(newWizard);

        Wizard savedWizard = new Wizard();
        savedWizard.setId(4);
        savedWizard.setName("blabla");


        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);

        //When and Then
        this.mockMvc.perform(post(this.baseUrl + "/wizards").contentType(MediaType.APPLICATION_JSON)
                .content(json).accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Add Success")).
                andExpect(jsonPath("$.data.name").value(savedWizard.getName())).
                andExpect(jsonPath("$.data.id").isNotEmpty());
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        //Given
        WizardDTO newWizard = new WizardDTO(4,"blabla",0);

        String json = objectMapper.writeValueAsString(newWizard);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(4);
        updatedWizard.setName("blabla");

        given(this.wizardService.update(eq(4), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        //When and Then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/4").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Update Success")).
                andExpect(jsonPath("$.data.name").value(updatedWizard.getName())).
                andExpect(jsonPath("$.data.id").isNotEmpty());
    }

    @Test
    void testUpdateWizardNotFound() throws Exception {
        //Given
        WizardDTO newWizard = new WizardDTO(7,"blabla",0);

        String json = objectMapper.writeValueAsString(newWizard);

        given(this.wizardService.update(eq(7), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 7));

        //When and Then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/7").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(false)).
                andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND)).
                andExpect(jsonPath("$.message").value("Could not find wizard with Id " + 7)).
                andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardByIdSuccess() throws Exception {
        //Given
        doNothing().when(this.wizardService).delete(1);

        //When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Delete Success")).
                andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardByIdNotFound() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("wizard",1)).when(this.wizardService).delete(1);

        //When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(false)).
                andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND)).
                andExpect(jsonPath("$.message").value("Could not find wizard with Id " + 1)).
                andExpect(jsonPath("$.data").isEmpty());
    }
}
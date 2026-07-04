package com.gestionSalon.dto.horaire;

import com.gestionSalon.entity.JourSemaine;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateHoraireTravailDTO {

    @NotNull(message = "Le jour est obligatoire.")
    private JourSemaine jourSemaine;

    @NotNull(message = "L'heure de début est obligatoire.")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire.")
    private LocalTime heureFin;
}
package com.gestionSalon.controller;

import com.gestionSalon.dto.prestation.PrestationPopulaireDTO;
import com.gestionSalon.dto.statistique.StatistiqueAnnulationDTO;
import com.gestionSalon.dto.statistique.TauxRemplissageDTO;
import com.gestionSalon.service.StatistiqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistiques")
@Tag(
        name = "Statistiques",
        description = """
                Consultation des indicateurs d'activité du salon.
                
                Ce module permet notamment :
                - d'identifier les prestations les plus demandées ;
                - de mesurer le taux de remplissage des prestataires ;
                - de suivre les annulations de rendez-vous.
                
                Toutes les statistiques sont réservées au gérant.
                """
)
@RequiredArgsConstructor
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @GetMapping("/prestations-populaires")
    @Operation(
            summary = "Consulter les prestations les plus populaires",
            description = """
                    Retourne le classement des prestations les plus demandées.
                    
                    Si aucun intervalle de dates n'est fourni,
                    les statistiques sont calculées sur l'ensemble
                    de l'historique disponible.
                    
                    Si une période est renseignée, seules les prestations
                    réalisées ou réservées dans cet intervalle sont prises en compte.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Classement récupéré avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrestationPopulaireDTO>>
    getPrestationsPopulaires(

            @Parameter(
                    description = "Date de début de la période d'analyse (optionnelle)",
                    example = "2026-07-01"
            )
            @RequestParam(required = false)
            LocalDate debut,

            @Parameter(
                    description = "Date de fin de la période d'analyse (optionnelle)",
                    example = "2026-07-31"
            )
            @RequestParam(required = false)
            LocalDate fin
    ) {

        return ResponseEntity.ok(
                statistiqueService.getPrestationsPopulaires(
                        debut,
                        fin
                )
        );
    }

    @GetMapping("/taux-remplissage")
    @Operation(
            summary = "Consulter le taux de remplissage",
            description = """
                    Calcule le taux de remplissage des prestataires
                    pour une journée donnée.
                    
                    Le calcul repose sur :
                    - les horaires de travail configurés ;
                    - les rendez-vous confirmés ou honorés ;
                    - le temps effectivement réservé.
                    
                    Le résultat est fourni pour chaque prestataire.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Taux de remplissage calculé avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TauxRemplissageDTO>>
    getTauxRemplissage(

            @Parameter(
                    description = "Date concernée par le calcul du taux de remplissage",
                    example = "2026-07-15",
                    required = true
            )
            @RequestParam
            LocalDate date
    ) {

        return ResponseEntity.ok(
                statistiqueService.getTauxRemplissage(date)
        );
    }

    @GetMapping("/annulations")
    @Operation(
            summary = "Consulter les statistiques d'annulation",
            description = """
                    Retourne les indicateurs liés aux annulations
                    de rendez-vous.
                    
                    Si aucune période n'est précisée,
                    l'ensemble de l'historique est pris en compte.
                    
                    Les statistiques peuvent inclure :
                    - le nombre total de rendez-vous ;
                    - le nombre d'annulations ;
                    - le taux d'annulation.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statistiques récupérées avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatistiqueAnnulationDTO>
    getStatistiquesAnnulations(

            @Parameter(
                    description = "Date de début de la période d'analyse (optionnelle)",
                    example = "2026-07-01"
            )
            @RequestParam(required = false)
            LocalDate debut,

            @Parameter(
                    description = "Date de fin de la période d'analyse (optionnelle)",
                    example = "2026-07-31"
            )
            @RequestParam(required = false)
            LocalDate fin
    ) {

        return ResponseEntity.ok(
                statistiqueService.getStatistiquesAnnulations(
                        debut,
                        fin
                )
        );
    }

}
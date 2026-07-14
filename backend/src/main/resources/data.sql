-- =====================================================
-- PERMISSIONS
-- =====================================================

INSERT INTO permissions(id, nom)
VALUES (1, 'USER_READ')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (2, 'USER_CREATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (3, 'USER_UPDATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (4, 'USER_DELETE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (5, 'ROLE_READ')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (6, 'ROLE_UPDATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (7, 'ROLE_PERMISSION_UPDATE')
    ON CONFLICT DO NOTHING;
INSERT INTO permissions(id, nom)
VALUES (8, 'PRESTATION_READ')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (9, 'PRESTATION_CREATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (10, 'PRESTATION_UPDATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (11, 'PRESTATION_DELETE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (12, 'HORAIRE_TRAVAIL_READ')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (13, 'HORAIRE_TRAVAIL_CREATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (14, 'HORAIRE_TRAVAIL_UPDATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (15, 'HORAIRE_TRAVAIL_DELETE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (16, 'HORAIRE_OUVERTURE_READ')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (17, 'HORAIRE_OUVERTURE_CREATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (18, 'HORAIRE_OUVERTURE_UPDATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (19, 'HORAIRE_OUVERTURE_DELETE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (20, 'RENDEZ_VOUS_READ')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (21, 'RENDEZ_VOUS_CREATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (22, 'RENDEZ_VOUS_UPDATE')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (23, 'RENDEZ_VOUS_CANCEL')
    ON CONFLICT DO NOTHING;

INSERT INTO permissions(id, nom)
VALUES (24, 'RENDEZ_VOUS_NO_SHOW')
    ON CONFLICT DO NOTHING;


-- =====================================================
-- ROLES
-- =====================================================

INSERT INTO roles(id, nom)
VALUES (1, 'ADMIN')
    ON CONFLICT DO NOTHING;

INSERT INTO roles(id, nom)
VALUES (2, 'PRESTATAIRE')
    ON CONFLICT DO NOTHING;

INSERT INTO roles(id, nom)
VALUES (3, 'CLIENT')
    ON CONFLICT DO NOTHING;


-- =====================================================
-- ROLE_PERMISSIONS
-- =====================================================

-- ADMIN : toutes les permissions

INSERT INTO role_permissions(role_id, permission_id)
SELECT 1, id
FROM permissions
    ON CONFLICT DO NOTHING;


-- PRESTATAIRE

INSERT INTO role_permissions(role_id, permission_id)
VALUES (2, 1)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (2, 20)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (2, 24)
    ON CONFLICT DO NOTHING;


-- CLIENT

INSERT INTO role_permissions(role_id, permission_id)
VALUES (3, 20)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (3, 21)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (3, 22)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (3, 23)
    ON CONFLICT DO NOTHING;


-- =====================================================
-- UTILISATEURS DE TEST
-- Mot de passe : admin123
-- prestataire123
-- client123
-- =====================================================

INSERT INTO utilisateurs(
    id,
    nom,
    prenom,
    telephone,
    mot_de_passe,
    actif,
    supprimee,
    date_creation,
    date_modification,
    role_id
)
VALUES(
          1,
          'Administrateur',
          'Systeme',
          '+22670000001',
          '$2a$10$tgFbJP/H4SSk/oc2wpupk.XWCdOHrD9XyTEMKFOMDfmWTm4BsKLPa',
          true,
          false,
          NOW(),
       NOW(),
          1
      )
    ON CONFLICT DO NOTHING;


INSERT INTO utilisateurs(
    id,
    nom,
    prenom,
    telephone,
    mot_de_passe,
    actif,
    supprimee,
    date_creation,
    date_modification,
    role_id
)
VALUES(
          2,
          'Sawadogo',
          'Moussa',
          '+22670000002',
          '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
          true,
          false,
          NOW(),
       NOW(),
          2
      )
    ON CONFLICT DO NOTHING;

INSERT INTO utilisateurs(
    id,
    nom,
    prenom,
    telephone,
    mot_de_passe,
    actif,
    supprimee,
    date_creation,
    date_modification,
    role_id
)
VALUES(
          3,
          'Sawadogo',
          'Moussa',
          '+22670000003',
          '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
          true,
          false,
          NOW(),
          NOW(),
          2
      )
    ON CONFLICT DO NOTHING;

-- =====================================================
-- PRESTATAIRES SUPPLÉMENTAIRES
-- Mot de passe : prestataire123
-- =====================================================

INSERT INTO utilisateurs(
    id,
    nom,
    prenom,
    telephone,
    mot_de_passe,
    actif,
    supprimee,
    date_creation,
    date_modification,
    role_id
)
VALUES
    (
        4,
        'Traore',
        'Aminata',
        '+22670000004',
        '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
        true,
        false,
        NOW(),
        NOW(),
        2
    ),
    (
        5,
        'Kabore',
        'Mariam',
        '+22670000005',
        '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
        true,
        false,
        NOW(),
        NOW(),
        2
    ),
    (
        6,
        'Zongo',
        'Issa',
        '+22670000006',
        '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
        true,
        false,
        NOW(),
        NOW(),
        2
    ),
    (
        7,
        'Ouedraogo',
        'Fatoumata',
        '+22670000007',
        '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
        true,
        false,
        NOW(),
        NOW(),
        2
    )
    ON CONFLICT DO NOTHING;

-- =====================================================
-- CLIENTS DE DEMONSTRATION
-- Mot de passe : client123
-- =====================================================

INSERT INTO utilisateurs(
    id,
    nom,
    prenom,
    telephone,
    mot_de_passe,
    actif,
    supprimee,
    date_creation,
    date_modification,
    role_id
)
VALUES

    (8,'Diallo','Oumar','+22670000008',
     '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
     true,false,NOW(),NOW(),3),

    (9,'Sawadogo','Awa','+22670000009',
     '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
     true,false,NOW(),NOW(),3),

    (10,'Yameogo','Ali','+22670000010',
     '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
     true,false,NOW(),NOW(),3),

    (11,'Kaboré','Mariam','+22670000011',
     '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
     true,false,NOW(),NOW(),3),

    (12,'Ouédraogo','Idrissa','+22670000012',
     '$2a$10$kPWcjIahisi4.RM57P4qVO8kQWg.ckiLcu5FdktU.ZHFw6ItGfy3K',
     true,false,NOW(),NOW(),3)

    ON CONFLICT DO NOTHING;

-- =====================================================
-- PRESTATIONS
-- =====================================================

INSERT INTO prestations (
    id,
    nom,
    duree_minutes,
    prix,
    categorie,
    description,
    actif,
    supprimee
)
VALUES
    (
        1,
        'Coupe Homme',
        30,
        3000,
        'COIFFURE_HOMME',
        'Coupe classique ou moderne pour homme avec finition soignée.',
        true,
        false
    ),

    (
        2,
        'Coupe Femme',
        60,
        7000,
        'COIFFURE_FEMME',
        'Coupe et mise en forme adaptées au style et à la morphologie de la cliente.',
        true,
        false
    ),

    (
        3,
        'Shampoing',
        20,
        2000,
        'SOIN',
        'Nettoyage et entretien du cuir chevelu avec produits adaptés.',
        true,
        false
    ),

    (
        4,
        'Barbe',
        15,
        1500,
        'COIFFURE_HOMME',
        'Taille, traçage et entretien de la barbe.',
        true,
        false
    ),

    (
        5,
        'Coloration',
        90,
        12000,
        'COLORATION',
        'Application de coloration pour changer ou raviver la couleur des cheveux.',
        true,
        false
    ),

    (
        6,
        'Tresses simples',
        120,
        15000,
        'COIFFURE_FEMME',
        'Réalisation de tresses simples pour un style élégant et durable.',
        true,
        false
    ),

    (
        7,
        'Tresses africaines',
        180,
        25000,
        'COIFFURE_FEMME',
        'Confection de tresses africaines traditionnelles avec finitions professionnelles.',
        true,
        false
    ),

    (
        8,
        'Lissage',
        120,
        20000,
        'SOIN',
        'Traitement permettant de lisser durablement les cheveux.',
        true,
        false
    ),

    (
        9,
        'Pose perruque',
        45,
        5000,
        'COIFFURE_FEMME',
        'Installation et ajustement professionnel de perruques ou extensions.',
        true,
        false
    ),

    (
        10,
        'Soin capillaire',
        60,
        8000,
        'SOIN',
        'Traitement nourrissant et réparateur pour renforcer les cheveux.',
        true,
        false
    )
    ON CONFLICT (id) DO NOTHING;


-- =====================================================
-- COMPETENCES DES PRESTATAIRES
-- =====================================================

    -- Moussa
INSERT INTO prestataire_prestations(prestataire_id, prestation_id)
VALUES
    (2,1),
    (2,3),
    (2,4)
    ON CONFLICT DO NOTHING;

  -- Aminata
INSERT INTO prestataire_prestations(prestataire_id, prestation_id)
VALUES
    (4,6),
    (4,7),
    (4,9)
    ON CONFLICT DO NOTHING;

-- Mariam
INSERT INTO prestataire_prestations(prestataire_id, prestation_id)
VALUES
    (5,2),
    (5,5),
    (5,8)
    ON CONFLICT DO NOTHING;

-- Issa
INSERT INTO prestataire_prestations(prestataire_id, prestation_id)
VALUES
    (6,1),
    (6,4),
    (6,10)
    ON CONFLICT DO NOTHING;

-- Fatoumata
INSERT INTO prestataire_prestations(prestataire_id, prestation_id)
VALUES
    (7,2),
    (7,5),
    (7,10)
    ON CONFLICT DO NOTHING;


-- =====================================================
-- HORAIRES D'OUVERTURE DU SALON
-- =====================================================

INSERT INTO horaires_ouverture (
    id,
    jour_semaine,
    heure_debut,
    heure_fin,
    supprimee,
    date_suppression
)
VALUES
    (1, 'LUNDI',    '08:00:00', '20:00:00', false, null),
    (2, 'MARDI',    '08:00:00', '20:00:00', false, null),
    (3, 'MERCREDI', '08:00:00', '20:00:00', false, null),
    (4, 'JEUDI',    '08:00:00', '20:00:00', false, null),
    (5, 'VENDREDI', '08:00:00', '20:00:00', false, null),
    (6, 'SAMEDI',   '08:00:00', '19:00:00', false, null),
    (7, 'DIMANCHE', '09:00:00', '16:00:00', false, null)
    ON CONFLICT (id) DO NOTHING;


-- =====================================================
-- Horaires de travail des prestataires
-- =====================================================
-- Moussa
INSERT INTO horaires_travail(
    id,
    prestataire_id,
    jour_semaine,
    heure_debut,
    heure_fin,
    supprimee
)
VALUES
    (1,2,'LUNDI','08:00','17:00',false),
    (2,2,'MARDI','08:00','17:00',false),
    (3,2,'MERCREDI','08:00','17:00',false),
    (4,2,'JEUDI','08:00','17:00',false),
    (5,2,'VENDREDI','08:00','17:00',false)

    ON CONFLICT (id) DO NOTHING;

-- Aminata
INSERT INTO horaires_travail(
    id,
    prestataire_id,
    jour_semaine,
    heure_debut,
    heure_fin,
    supprimee
)
VALUES
    (6,4,'MARDI','09:00','18:00',false),
    (7,4,'MERCREDI','09:00','18:00',false),
    (8,4,'JEUDI','09:00','18:00',false),
    (9,4,'VENDREDI','09:00','18:00',false),
    (10,4,'SAMEDI','09:00','18:00',false)

    ON CONFLICT (id) DO NOTHING;

-- Mariame
INSERT INTO horaires_travail(
    id,
    prestataire_id,
    jour_semaine,
    heure_debut,
    heure_fin,
    supprimee
)
VALUES
    (11,5,'LUNDI','10:00','19:00',false),
    (12,5,'MARDI','10:00','19:00',false),
    (13,5,'MERCREDI','10:00','19:00',false),
    (14,5,'JEUDI','10:00','19:00',false),
    (15,5,'VENDREDI','10:00','19:00',false)

    ON CONFLICT (id) DO NOTHING;

-- Issa
INSERT INTO horaires_travail(
    id,
    prestataire_id,
    jour_semaine,
    heure_debut,
    heure_fin,
    supprimee
)
VALUES
    (16,6,'LUNDI','08:00','16:00',false),
    (17,6,'MARDI','08:00','16:00',false),
    (18,6,'MERCREDI','08:00','16:00',false),
    (19,6,'JEUDI','08:00','16:00',false),
    (20,6,'VENDREDI','08:00','16:00',false),
    (21,6,'SAMEDI','08:00','14:00',false)

    ON CONFLICT (id) DO NOTHING;

-- Fatoumata
INSERT INTO horaires_travail(
    id,
    prestataire_id,
    jour_semaine,
    heure_debut,
    heure_fin,
    supprimee
)
VALUES
    (22,7,'MARDI','09:00','17:00',false),
    (23,7,'MERCREDI','09:00','17:00',false),
    (24,7,'JEUDI','09:00','17:00',false),
    (25,7,'VENDREDI','09:00','17:00',false),
    (26,7,'SAMEDI','09:00','17:00',false)

    ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- Rendez-vous de démonstration
-- =====================================================

INSERT INTO rendez_vous(
    id,
    client_id,
    prestataire_id,
    prestation_id,
    date_rendez_vous,
    heure_debut,
    heure_fin,
    statut,
    notes,
    date_creation,
    supprimee
)
VALUES

-- HONORES

(1,8,2,1,CURRENT_DATE-20,'09:00','09:30','HONORE',null,NOW(),false),
(2,9,2,4,CURRENT_DATE-19,'10:00','10:15','HONORE',null,NOW(),false),
(3,10,4,6,CURRENT_DATE-18,'09:00','11:00','HONORE',null,NOW(),false),
(4,11,4,7,CURRENT_DATE-17,'13:00','16:00','HONORE',null,NOW(),false),
(5,12,5,5,CURRENT_DATE-16,'09:00','10:30','HONORE',null,NOW(),false),

(6,8,5,8,CURRENT_DATE-15,'11:00','13:00','HONORE',null,NOW(),false),
(7,9,6,1,CURRENT_DATE-14,'08:00','08:30','HONORE',null,NOW(),false),
(8,10,6,4,CURRENT_DATE-13,'09:00','09:15','HONORE',null,NOW(),false),
(9,11,7,5,CURRENT_DATE-12,'10:00','11:30','HONORE',null,NOW(),false),
(10,12,7,10,CURRENT_DATE-11,'14:00','15:00','HONORE',null,NOW(),false),

-- ANNULES

(11,8,2,1,CURRENT_DATE-8,'10:00','10:30','ANNULE',
 'Annulation client',NOW(),false),

(12,9,4,6,CURRENT_DATE-7,'09:00','11:00','ANNULE',
 'Empêchement',NOW(),false),

(13,10,5,5,CURRENT_DATE-6,'15:00','16:30','ANNULE',
 'Indisponibilité',NOW(),false),

-- NO SHOW

(14,11,2,4,CURRENT_DATE-5,'09:00','09:15','NO_SHOW',
 'Client absent',NOW(),false),

(15,12,6,1,CURRENT_DATE-4,'11:00','11:30','NO_SHOW',
 'Client absent',NOW(),false),

-- FUTURS CONFIRMES

(16,8,2,1,CURRENT_DATE+1,'09:00','09:30','CONFIRME',
 null,NOW(),false),

(17,9,4,6,CURRENT_DATE+2,'10:00','12:00','CONFIRME',
 null,NOW(),false),

(18,10,5,5,CURRENT_DATE+3,'14:00','15:30','CONFIRME',
 null,NOW(),false),

(19,11,7,10,CURRENT_DATE+4,'09:00','10:00','CONFIRME',
 null,NOW(),false),

(20,12,6,4,CURRENT_DATE+5,'11:00','11:15','CONFIRME',
 null,NOW(),false)

    ON CONFLICT DO NOTHING;

-- =====================================================
-- RESYNCHRONISATION DES SEQUENCES POSTGRESQL
-- =====================================================

SELECT setval(
               'permissions_id_seq',
               (SELECT MAX(id) FROM permissions)
       );

SELECT setval(
               'roles_id_seq',
               (SELECT MAX(id) FROM roles)
       );

SELECT setval(
               'utilisateurs_id_seq',
               (SELECT MAX(id) FROM utilisateurs)
       );

SELECT setval(
               'prestations_id_seq',
               (SELECT MAX(id) FROM prestations)
       );

SELECT setval(
               'horaires_ouverture_id_seq',
               (SELECT MAX(id) FROM horaires_ouverture)
       );

SELECT setval(
               'horaires_travail_id_seq',
               (SELECT MAX(id) FROM horaires_travail)
       );


SELECT setval(
               'rendez_vous_id_seq',
               (SELECT MAX(id) FROM rendez_vous)
       );

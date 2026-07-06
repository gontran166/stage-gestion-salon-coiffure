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
          'Ouedraogo',
          'Adama',
          '+22670000003',
          '$2a$10$PWLfI3T1ynvfm2rzOjDKy.Q33RkE9ikR4R3vK95cs.Z.L9oBMsjge',
          true,
          false,
          NOW(),
       NOW(),
          3
      )
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

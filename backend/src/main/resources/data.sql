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

-- ADMIN

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 1)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 2)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 3)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 4)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 5)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 6)
    ON CONFLICT DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 7)
    ON CONFLICT DO NOTHING;


-- PRESTATAIRE

INSERT INTO role_permissions(role_id, permission_id)
VALUES (2, 1)
    ON CONFLICT DO NOTHING;


-- CLIENT

-- aucune permission spécifique pour l'instant


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
    email,
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
          'admin@salon.com',
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
    email,
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
          'prestataire@salon.com',
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
    email,
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
          'client@salon.com',
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
               COALESCE((SELECT MAX(id) FROM permissions), 1)
       );

SELECT setval(
               'roles_id_seq',
               COALESCE((SELECT MAX(id) FROM roles), 1)
       );

SELECT setval(
               'utilisateurs_id_seq',
               COALESCE((SELECT MAX(id) FROM utilisateurs), 1)
       );

-- Vérifier l'existence de la table products
SELECT * FROM SYS.SYSTABLES WHERE TABLENAME = 'PRODUCTS';

-- Afficher la structure de la table products
SELECT
    COLNAME,
    COLUMNNUMBER,
    COLUMNDATATYPE
FROM SYS.SYSCOLUMNS
WHERE
    TABLEID IN (
        SELECT TABLEID
        FROM SYS.SYSTABLES
        WHERE
            TABLENAME = 'PRODUCTS'
    );

-- Afficher tous les produits
SELECT * FROM products;

-- Compter les produits
SELECT COUNT(*) FROM products;
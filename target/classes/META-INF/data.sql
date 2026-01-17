-- Insertion de données de test pour les produits
INSERT INTO
    products (
        sku,
        name,
        location_code,
        location_note,
        stock_quantity,
        updated_at,
        version
    )
VALUES (
        'SKU001',
        'Chaise Bureau Ergonomique',
        'A1-01',
        'Zone A - Rangée 1',
        45,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU002',
        'Bureau Blanc 120cm',
        'A1-02',
        'Zone A - Rangée 1',
        12,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU003',
        'Lampe LED 60W',
        'A2-01',
        'Zone A - Rangée 2',
        78,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU004',
        'Clavier Mécanique RGB',
        'B1-01',
        'Zone B - Rangée 1',
        0,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU005',
        'Souris Sans Fil Logitech',
        'B1-02',
        'Zone B - Rangée 1',
        156,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU006',
        'Écran 27 pouces',
        'C1-01',
        'Zone C - Rangée 1',
        8,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU007',
        'Casque Audio Professionnel',
        'C2-01',
        'Zone C - Rangée 2',
        0,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU008',
        'Support Laptop Aluminium',
        'D1-01',
        'Zone D - Rangée 1',
        34,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU009',
        'Câble USB-C 2m',
        'D1-02',
        'Zone D - Rangée 1',
        250,
        CURRENT_TIMESTAMP(),
        0
    ),
    (
        'SKU010',
        'Adaptateur HDMI',
        'D2-01',
        'Zone D - Rangée 2',
        5,
        CURRENT_TIMESTAMP(),
        0
    );

-- Insertion de données de test pour les ventes
INSERT INTO
    sales_records (
        sku_sold,
        quantity_sold,
        sale_amount,
        sale_date,
        created_at
    )
VALUES (
        'SKU001',
        2,
        299.98,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    ),
    (
        'SKU003',
        3,
        89.97,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    ),
    (
        'SKU005',
        5,
        249.95,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    ),
    (
        'SKU002',
        1,
        449.99,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    ),
    (
        'SKU006',
        2,
        1099.98,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    ),
    (
        'SKU009',
        10,
        149.90,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    ),
    (
        'SKU001',
        1,
        149.99,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    ),
    (
        'SKU008',
        3,
        389.97,
        CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()
    );
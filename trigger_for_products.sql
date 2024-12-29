
DELIMITER //

-- Create the trigger
CREATE TRIGGER before_product_insert
BEFORE INSERT ON products
FOR EACH ROW
BEGIN
    -- Set the status, created_on, and updated_on values
    SET NEW.status = TRUE;
    SET NEW.created_on = NOW();
    SET NEW.updated_on = NOW();
END;
//

-- Reset the delimiter back to default
DELIMITER ;


DELIMITER //

-- Create the trigger
CREATE TRIGGER before_variant_insert
BEFORE INSERT ON variants
FOR EACH ROW
BEGIN
    -- Set the status, created_on, and updated_on values
    SET NEW.status = TRUE;
    SET NEW.created_on = NOW();
    SET NEW.updated_on = NOW();
END;
//

-- Reset the delimiter back to default
DELIMITER ;
DELIMITER //

-- Create the trigger
CREATE TRIGGER before_variant_update
BEFORE update ON variants
FOR EACH ROW
BEGIN

    SET NEW.updated_on = NOW();
END;
//

DELIMITER //

CREATE TRIGGER before_product_update
BEFORE UPDATE ON products
FOR EACH ROW
BEGIN
    SET NEW.updated_on = NOW();
END;
//

DELIMITER ;

DELIMITER $$

CREATE TRIGGER generate_sku_before_insert
BEFORE INSERT ON variants
FOR EACH ROW
BEGIN
    DECLARE max_sku INT;
    DECLARE new_sku VARCHAR(10);
    
    -- Check if SKU is empty
    IF NEW.sku IS NULL OR NEW.sku = '' THEN
        -- Find the maximum numeric SKU value
        SELECT IFNULL(MAX(CAST(SUBSTRING(sku, 4) AS UNSIGNED)), 0) INTO max_sku
        FROM variants
        WHERE sku LIKE 'PVN%';
        
        -- Increment SKU and assign it to the new record
        SET max_sku = max_sku + 1;
        SET new_sku = CONCAT('PVN', LPAD(max_sku, 5, '0'));
        SET NEW.sku = new_sku;
    END IF;
END
$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER generate_category_code_before_insert
BEFORE INSERT ON categories
FOR EACH ROW
BEGIN
    DECLARE max_code INT;
    DECLARE new_code VARCHAR(10);
    
    -- Check if CODE is empty
    IF NEW.code IS NULL OR NEW.code = '' THEN
        -- Find the maximum numeric CODE value
        SELECT IFNULL(MAX(CAST(SUBSTRING(code, 4) AS UNSIGNED)), 0) INTO max_code
        FROM categories
        WHERE code LIKE 'PGN%';
        
        -- Increment CODE and assign it to the new record
        SET max_code = max_code + 1;
        SET new_code = CONCAT('PGN', LPAD(max_code, 5, '0'));
        SET NEW.code = new_code;
    END IF;
END
$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER generate_brand_code_before_insert
BEFORE INSERT ON brands
FOR EACH ROW
BEGIN
    DECLARE max_code INT;
    DECLARE new_code VARCHAR(10);
    
    -- Check if CODE is empty
    IF NEW.code IS NULL OR NEW.code = '' THEN
        -- Find the maximum numeric CODE value
        SELECT IFNULL(MAX(CAST(SUBSTRING(code, 4) AS UNSIGNED)), 0) INTO max_code
        FROM brands
        WHERE code LIKE 'PBN%';
        
        -- Increment CODE and assign it to the new record
        SET max_code = max_code + 1;
        SET new_code = CONCAT('PBN', LPAD(max_code, 5, '0'));
        SET NEW.code = new_code;
    END IF;
END
$$

DELIMITER ;
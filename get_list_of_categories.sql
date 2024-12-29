CREATE DEFINER=`root`@`localhost` PROCEDURE `get_list_of_categories`(
	IN p_page INT, -- page number pagination
	IN p_limit INT, -- number of category per page
    IN query_string VARCHAR(256) -- search by category name
)
BEGIN
	DECLARE offsett INT;
    SET offsett = (p_page - 1) * p_limit;

    SET @sql = 'SELECT * FROM categories where status = true ';
    IF query_string IS NOT NULL AND query_string != '' THEN
		SET @sql = CONCAT(@sql, ' AND name LIKE ', QUOTE(CONCAT('%', query_string, '%')));
    END IF;
    SET @sql = CONCAT(@sql, ' ORDER BY updated_on DESC ');
	SET @sql = CONCAT(@sql, ' LIMIT ', p_limit, ' OFFSET ',offsett);
    
    
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
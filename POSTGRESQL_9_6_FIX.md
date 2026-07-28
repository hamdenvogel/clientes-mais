# PostgreSQL 9.6 Compatibility Fix

## Problem

The application was throwing SQL syntax errors when running against PostgreSQL 9.6.2:

```
ERROR: syntax error at or near "$2"
SQL: select ... offset ? rows fetch first ? rows only
```

This happens because:
- **Hibernate 6.4+** (used in Spring Boot 3.2.5) defaults to using the modern PostgreSQL pagination syntax: `OFFSET ? rows FETCH FIRST ? rows ONLY`
- **PostgreSQL 9.6** does not support this syntax (it was introduced in PostgreSQL 13+)
- PostgreSQL 9.6 only supports the older syntax: `LIMIT ? OFFSET ?`

## Solution

The fix consists of two parts:

### 1. Custom Hibernate Dialect (`CustomPostgreSQLDialect.java`)

A custom PostgreSQL dialect that:
- Explicitly sets the PostgreSQL version to 9.6
- Overrides `getLimitHandler()` to force `LimitOffsetLimitHandler` (which generates `LIMIT/OFFSET` syntax)
- Declares support for `LIMIT` and `OFFSET` clauses in subqueries

```java
public class CustomPostgreSQLDialect extends PostgreSQLDialect {
    public CustomPostgreSQLDialect() {
        super(DatabaseVersion.make(9, 6)); // Force version 9.6
    }

    @Override
    public LimitHandler getLimitHandler() {
        // Forces LIMIT ? OFFSET ? syntax (compatible with PostgreSQL 9.6)
        return LimitOffsetLimitHandler.INSTANCE;
    }
}
```

### 2. Hibernate Configuration Properties

Added the critical property to prevent Hibernate from auto-detecting the database version:

**In `application.properties` and `application-dev.properties`:**

```properties
spring.jpa.properties.hibernate.use_jdbc_metadata_for_version_detection=false
```

This prevents Hibernate 6.4+ from overriding our custom dialect with its auto-detected version.

## Files Modified

1. **`clientes-api/src/main/resources/application.properties`**
   - Added: `spring.jpa.properties.hibernate.use_jdbc_metadata_for_version_detection=false`

2. **`clientes-api/src/main/resources/application-dev.properties`**
   - Added: `spring.jpa.properties.hibernate.use_jdbc_metadata_for_version_detection=false`

3. **`clientes-api/src/main/java/io/github/hvogel/clientes/config/CustomPostgreSQLDialect.java`**
   - Updated documentation and added `supportsLimitClause()` method for completeness

## Verification

After applying these changes:
1. Rebuild the application: `mvn clean install`
2. Start the application
3. Look for the initialization message in console output:
   ```
   ###################################################
     LOADING CUSTOM DIALECT: CustomPostgreSQLDialect  
     FORCING POSTGRESQL VERSION: 9.6                  
     PAGING STRATEGY: LIMIT/OFFSET (FORCED)           
   ###################################################
   ```

4. Test pagination endpoints - they should now work with PostgreSQL 9.6 using the correct `LIMIT/OFFSET` syntax

## Technical Details

- **Version Detection Disabled**: The property `hibernate.use_jdbc_metadata_for_version_detection=false` ensures Hibernate uses the dialect we specified, not the version it detects from the database driver
- **Explicit Dialect Assignment**: Both `spring.jpa.database-platform` and `spring.jpa.properties.hibernate.dialect` are configured with the custom dialect
- **Backward Compatible**: This solution is specific to PostgreSQL 9.6 but doesn't break compatibility with newer versions if you upgrade later (though newer versions could use a different dialect)

## SQL Query Examples

**Before Fix (causes error):**
```sql
SELECT ... FROM cliente ORDER BY nome OFFSET ? ROWS FETCH FIRST ? ROWS ONLY
```

**After Fix (works):**
```sql
SELECT ... FROM cliente ORDER BY nome LIMIT ? OFFSET ?
```

## References

- Hibernate 6.4+ Dialect Documentation
- PostgreSQL 9.6 Documentation: [Pagination Support](https://www.postgresql.org/docs/9.6/sql-select.html)
- PostgreSQL 13+ Documentation: OFFSET ... ROWS FETCH syntax


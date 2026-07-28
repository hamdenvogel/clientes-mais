# How to Apply the PostgreSQL 9.6 Compatibility Fix

## Summary of Changes

This fix resolves the SQL syntax error `ERROR: syntax error at or near "$2"` that occurs when using Hibernate 6.4+ with PostgreSQL 9.6.2.

### Root Cause
Hibernate 6.4+ (included in Spring Boot 3.2.5) generates `OFFSET ? ROWS FETCH FIRST ? ROWS ONLY` syntax by default, which is only supported in PostgreSQL 13+. PostgreSQL 9.6 only supports `LIMIT ? OFFSET ?` syntax.

## Files Modified

### 1. **application.properties**
Added property to prevent Hibernate version auto-detection:
```properties
spring.jpa.properties.hibernate.use_jdbc_metadata_for_version_detection=false
```

### 2. **application-dev.properties**
Added same property in development configuration:
```properties
spring.jpa.properties.hibernate.use_jdbc_metadata_for_version_detection=false
```

### 3. **CustomPostgreSQLDialect.java**
The custom dialect is already present and properly configured. It:
- Sets PostgreSQL version to 9.6 explicitly
- Forces `LimitOffsetLimitHandler` for LIMIT/OFFSET pagination
- Declares support for OFFSET in subqueries

## Deployment Steps

1. **Pull the latest code** with these changes
2. **Clean rebuild**: `mvn clean install`
3. **Start the application** and verify the console output shows:
   ```
   ###################################################
     LOADING CUSTOM DIALECT: CustomPostgreSQLDialect  
     FORCING POSTGRESQL VERSION: 9.6                  
     PAGING STRATEGY: LIMIT/OFFSET (FORCED)           
   ###################################################
   ```
4. **Test endpoints** that use pagination (e.g., `/api/clientes/pesquisa-paginada`)

## Verification Checklist

- [ ] Application starts without errors
- [ ] Custom dialect initialization message appears in console
- [ ] Pagination endpoints return results successfully
- [ ] No SQL syntax errors in PostgreSQL logs
- [ ] No "OFFSET ? ROWS FETCH FIRST" syntax in query logs

## Rollback Plan

If needed to rollback:
1. Remove the `use_jdbc_metadata_for_version_detection=false` property
2. The application will still work but may have issues with pagination on PostgreSQL 9.6

## Technical Notes

- This configuration is safe for PostgreSQL 9.6 through 12
- If upgrading to PostgreSQL 13+, you could create a new dialect class or adjust this configuration
- The `use_jdbc_metadata_for_version_detection=false` property is critical - without it, Hibernate will ignore our version setting

## Support

If issues persist:
1. Check PostgreSQL version: `SELECT version();`
2. Enable SQL logging: `spring.jpa.properties.hibernate.show_sql=true` (already enabled in dev)
3. Verify dialect is loaded from console output
4. Check that both `spring.jpa.database-platform` and `spring.jpa.properties.hibernate.dialect` are set correctly


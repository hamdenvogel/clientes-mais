
package io.github.hvogel.clientes.infra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InfraCoverageTest {

    @Test
    void testCommonServiceDefaults() {
        // Use String as D because it implements Serializable
        CommonService<IBaseEntity, String> service = new CommonService<>() {
            @Override
            public IBaseEntity save(IBaseEntity entity) {
                return null;
            }

            @Override
            public void delete(IBaseEntity entity) {
            }

            @Override
            public void deleteById(Long id) {
            }

            @Override
            public java.util.List<IBaseEntity> findAll() {
                return null;
            }

            @Override
            public java.util.Optional<IBaseEntity> findOneById(Long id) {
                return java.util.Optional.empty();
            }

            @Override
            public org.springframework.data.domain.Page<IBaseEntity> findAll(
                    org.springframework.data.domain.Pageable pageable) {
                return null;
            }

            @Override
            public IBaseEntity saveAndFlush(IBaseEntity entity) {
                return null;
            }

            @Override
            public org.springframework.data.domain.Page<IBaseEntity> findByExample(
                    org.springframework.data.domain.Example<IBaseEntity> ex,
                    org.springframework.data.domain.Pageable pageable) {
                return null;
            }

            @Override
            public java.util.Optional<IBaseEntity> findOneByExample(
                    org.springframework.data.domain.Example<IBaseEntity> ex) {
                return java.util.Optional.empty();
            }

            @Override
            public String convertToDto(IBaseEntity entity) {
                return null;
            }

            @Override
            public IBaseEntity convertToEntity(String dto) {
                return null;
            }
        };

        assertDoesNotThrow(() -> service.onBeforeSave(null));
        assertDoesNotThrow(() -> service.onAfterSave(null));
    }
}

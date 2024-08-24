package droni.backend.api.common;

@FunctionalInterface
public interface DroniServiceSupplier {
    DroniService getDroniServiceDto(Long id);
}

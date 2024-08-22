package droni.backend.api.common;

@FunctionalInterface
public interface DroniServiceSupplier {
    DroniServiceDto getDroniServiceDto(Long id);
}

package microservice.ProcessEvaluation.Interfaces;

/**
 * Generic mapper interface for converting entities into DTOs.
 *
 * @param <T> the source entity type
 * @param <R> the destination DTO type
 */
public interface IMapper<T, R> {

    /**
     * Converts an entity into its corresponding DTO representation.
     *
     * @param pEntity the entity to convert
     * @return the resulting DTO
     */
    R toDto(T pEntity);
}

package microservice.ProcessEvaluation.Interfaces;

public interface IMapper<T,R>{
    R toDto(T pEntity);
}
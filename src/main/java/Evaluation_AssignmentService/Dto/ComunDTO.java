package Evaluation_AssignmentService.Dto;

import Evaluation_AssignmentService.Enum.EnumProcessStatus;

import java.io.Serializable;

public class ComunDTO implements Serializable {
    private Long id;
    private EnumProcessStatus status;

    public ComunDTO() {}
    public ComunDTO(Long id, EnumProcessStatus status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public EnumProcessStatus getAction() {
        return status;
    }
}
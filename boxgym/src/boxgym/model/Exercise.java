package boxgym.model;

import java.time.LocalDateTime;

public class Exercise {

    private int exerciseId; //Identificador
    private String name; //Nome
    private String abbreviation; //Abreviação
    private String exerciseType; //Tipo
    private String exerciseGroup; //Grupo
    private String description; //Descrição
    private String instruction; //Instrução
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    public Exercise() {

    }

    //Construtor da CombBox
    public Exercise(int exerciseId, String name) {
        this.exerciseId = exerciseId;
        this.name = name;
    }

    //Construtor CREATE
    public Exercise(String name, String abbreviation, String exerciseType, String exerciseGroup, String description, String instruction) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.exerciseType = exerciseType;
        this.exerciseGroup = exerciseGroup;
        this.description = description;
        this.instruction = instruction;
    }

    //Construtor  UPDATE
    public Exercise(int exerciseId, String name, String abbreviation, String exerciseType, String exerciseGroup, String description, String instruction) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.abbreviation = abbreviation;
        this.exerciseType = exerciseType;
        this.exerciseGroup = exerciseGroup;
        this.description = description;
        this.instruction = instruction;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getExerciseGroup() {
        return exerciseGroup;
    }

    public void setExerciseGroup(String exerciseGroup) {
        this.exerciseGroup = exerciseGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}

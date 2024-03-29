package boxgym.model;

import java.time.LocalDateTime;

public class Workout {

    private int workoutId; //Identificador
    private String description; //Descrição
    private String goal; //Objetivo
    private int sessions; //Sessões
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    public Workout() {

    }
    
    //Construtor da CombBox
    public Workout(int workoutId, String description) {
        this.workoutId = workoutId;
        this.description = description;
    }

    //Construtor CREATE
    public Workout(String description, String goal, int sessions) {
        this.description = description;
        this.goal = goal;
        this.sessions = sessions;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public int getSessions() {
        return sessions;
    }

    public void setSessions(int sessions) {
        this.sessions = sessions;
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

package boxgym.model;

import java.time.LocalDateTime;

public class WorkoutExercise {

    private int workoutExerciseId; //Identificador
    private int fkWorkout; //Treino
    private int fkExercise; //Exercício
    private int sets; //Séries
    private int reps; //Repetições
    private int rest; //Descanso
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    private String tempExerciseName;

    public WorkoutExercise() {

    }

    //Construtor ObservableList e CREATE
    public WorkoutExercise(int fkWorkout, int fkExercise, int sets, int reps, int rest) {
        this.fkWorkout = fkWorkout;
        this.fkExercise = fkExercise;
        this.sets = sets;
        this.reps = reps;
        this.rest = rest;
    }

    public int getWorkoutExerciseId() {
        return workoutExerciseId;
    }

    public void setWorkoutExerciseId(int workoutExerciseId) {
        this.workoutExerciseId = workoutExerciseId;
    }

    public int getFkWorkout() {
        return fkWorkout;
    }

    public void setFkWorkout(int fkWorkout) {
        this.fkWorkout = fkWorkout;
    }

    public int getFkExercise() {
        return fkExercise;
    }

    public void setFkExercise(int fkExercise) {
        this.fkExercise = fkExercise;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public String getTempExerciseName() {
        return tempExerciseName;
    }

    public void setTempExerciseName(String tempExerciseName) {
        this.tempExerciseName = tempExerciseName;
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

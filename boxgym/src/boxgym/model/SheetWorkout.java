package boxgym.model;

import java.time.LocalDateTime;

public class SheetWorkout {

    private int sheetWorkoutId; //Identificador
    private int fkSheet; //Ficha
    private int fkWorkout; //Treino
    private String dayOfTheWeek; //Dia da semana
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em
    
    private String tempWorkoutDescription;

    public SheetWorkout() {

    }

    //Construtor CREATE
    public SheetWorkout(int fkSheet, int fkWorkout, String dayOfTheWeek) {
        this.fkSheet = fkSheet;
        this.fkWorkout = fkWorkout;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public int getSheetWorkoutId() {
        return sheetWorkoutId;
    }

    public void setSheetWorkoutId(int sheetWorkoutId) {
        this.sheetWorkoutId = sheetWorkoutId;
    }

    public int getFkSheet() {
        return fkSheet;
    }

    public void setFkSheet(int fkSheet) {
        this.fkSheet = fkSheet;
    }

    public int getFkWorkout() {
        return fkWorkout;
    }

    public void setFkWorkout(int fkWorkout) {
        this.fkWorkout = fkWorkout;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
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

    public String getTempWorkoutDescription() {
        return tempWorkoutDescription;
    }

    public void setTempWorkoutDescription(String tempWorkoutDescription) {
        this.tempWorkoutDescription = tempWorkoutDescription;
    }

}

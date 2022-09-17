package boxgym.model;

public class WorkoutExercise {

    private int workoutExerciseId; //Identificador
    private int fkWorkout; //Treino
    private int fkExercise; //Exercício
    private int sets; //Séries
    private int reps; //Repetições
    private int rest; //Descanso

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

}

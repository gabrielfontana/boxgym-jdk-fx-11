package boxgym.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Measurement {

    private int measurementId; //Identificador
    private int fkCustomer; //Cliente
    private LocalDate measurementDate; //Data
    private int height; //Altura
    private float weight; //Peso
    private float neck; //Pescoço
    private float shoulder; //Ombro
    private float rightArm; //Braço direito
    private float leftArm; //Braço esquerdo
    private float rightForearm; //Antebraço direito
    private float leftForearm; //Antebraço esquerdo
    private float thorax; //Tórax
    private float waist; //Cintura
    private float abdomen; //Abdome
    private float hip; //Quadril
    private float rightThigh; //Coxa direita
    private float leftThigh; //Coxa esquerda
    private float rightCalf; //Panturrilha direita
    private float leftCalf; //Panturrilha esquerda
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    private String tempCustomerName;
    
    public Measurement(){
        
    }
    
    //Construtor CREATE
    public Measurement(int fkCustomer, LocalDate measurementDate, int height, float weight,
            float neck, float shoulder, float rightArm, float leftArm, float rightForearm, float leftForearm, float thorax,
            float waist, float abdomen, float hip, float rightThigh, float leftThigh, float rightCalf, float leftCalf) {
        this.fkCustomer = fkCustomer;
        this.measurementDate = measurementDate;
        this.height = height;
        this.weight = weight;
        this.neck = neck;
        this.shoulder = shoulder;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightForearm = rightForearm;
        this.leftForearm = leftForearm;
        this.thorax = thorax;
        this.waist = waist;
        this.abdomen = abdomen;
        this.hip = hip;
        this.rightThigh = rightThigh;
        this.leftThigh = leftThigh;
        this.rightCalf = rightCalf;
        this.leftCalf = leftCalf;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    public int getFkCustomer() {
        return fkCustomer;
    }

    public void setFkCustomer(int fkCustomer) {
        this.fkCustomer = fkCustomer;
    }

    public LocalDate getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDate measurementDate) {
        this.measurementDate = measurementDate;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getNeck() {
        return neck;
    }

    public void setNeck(float neck) {
        this.neck = neck;
    }

    public float getShoulder() {
        return shoulder;
    }

    public void setShoulder(float shoulder) {
        this.shoulder = shoulder;
    }

    public float getRightArm() {
        return rightArm;
    }

    public void setRightArm(float rightArm) {
        this.rightArm = rightArm;
    }

    public float getLeftArm() {
        return leftArm;
    }

    public void setLeftArm(float leftArm) {
        this.leftArm = leftArm;
    }

    public float getRightForearm() {
        return rightForearm;
    }

    public void setRightForearm(float rightForearm) {
        this.rightForearm = rightForearm;
    }

    public float getLeftForearm() {
        return leftForearm;
    }

    public void setLeftForearm(float leftForearm) {
        this.leftForearm = leftForearm;
    }

    public float getThorax() {
        return thorax;
    }

    public void setThorax(float thorax) {
        this.thorax = thorax;
    }

    public float getWaist() {
        return waist;
    }

    public void setWaist(float waist) {
        this.waist = waist;
    }

    public float getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(float abdomen) {
        this.abdomen = abdomen;
    }

    public float getHip() {
        return hip;
    }

    public void setHip(float hip) {
        this.hip = hip;
    }

    public float getRightThigh() {
        return rightThigh;
    }

    public void setRightThigh(float rightThigh) {
        this.rightThigh = rightThigh;
    }

    public float getLeftThigh() {
        return leftThigh;
    }

    public void setLeftThigh(float leftThigh) {
        this.leftThigh = leftThigh;
    }

    public float getRightCalf() {
        return rightCalf;
    }

    public void setRightCalf(float rightCalf) {
        this.rightCalf = rightCalf;
    }

    public float getLeftCalf() {
        return leftCalf;
    }

    public void setLeftCalf(float leftCalf) {
        this.leftCalf = leftCalf;
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

    public String getTempCustomerName() {
        return tempCustomerName;
    }

    public void setTempCustomerName(String tempCustomerName) {
        this.tempCustomerName = tempCustomerName;
    }

}

package boxgym.model;

import java.time.LocalDateTime;

public class Customer {

    private int customerId; //Identificador
    private String personRegistry; //CPF
    private String name; //Nome
    private String sex; //Sexo
    private String email;   //E-mail
    private String phone; //Telefone
    private String zipCode; //CEP
    private String address; //Endereço
    private String addressComplement; //Complemento
    private String district; //Bairro
    private String city; //Cidade
    private String federativeUnit; //Unidade Federativa
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    public Customer() {

    }
    
    //Construtor CREATE
    public Customer(String personRegistry, String name, String sex, String email, String phone, String zipCode, String address,
            String addressComplement, String district, String city, String federativeUnit) {
        this.personRegistry = personRegistry;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressComplement = addressComplement;
        this.district = district;
        this.city = city;
        this.federativeUnit = federativeUnit;
    }
    
    //Construtor UPDATE
    public Customer(int customerId, String personRegistry, String name, String sex, String email, String phone, String zipCode, String address,
            String addressComplement, String district, String city, String federativeUnit) {
        this.customerId = customerId;
        this.personRegistry = personRegistry;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressComplement = addressComplement;
        this.district = district;
        this.city = city;
        this.federativeUnit = federativeUnit;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPersonRegistry() {
        return personRegistry;
    }

    public void setPersonRegistry(String personRegistry) {
        this.personRegistry = personRegistry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressComplement() {
        return addressComplement;
    }

    public void setAddressComplement(String addressComplement) {
        this.addressComplement = addressComplement;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFederativeUnit() {
        return federativeUnit;
    }

    public void setFederativeUnit(String federativeUnit) {
        this.federativeUnit = federativeUnit;
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

package boxgym.model;

import java.time.LocalDateTime;

public class Supplier {

    private int supplierId; //Identificador
    private String companyRegistry; //CNPJ
    private String corporateName; //Razão Social
    private String tradeName;   //Nome Fantasia
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

    public Supplier() {

    }

    //Construtor da CombBox
    public Supplier(int supplierId, String corporateName) {
        this.supplierId = supplierId;
        this.corporateName = corporateName;
    }

    //Construtor CREATE
    public Supplier(String companyRegistry, String corporateName, String tradeName, String email, String phone,
            String zipCode, String address, String addressComplement, String district, String city, String federativeUnit) {
        this.companyRegistry = companyRegistry;
        this.corporateName = corporateName;
        this.tradeName = tradeName;
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
    public Supplier(int supplierId, String corporateName, String tradeName, String email, String phone,
            String zipCode, String address, String addressComplement, String district, String city, String federativeUnit) {
        this.supplierId = supplierId;
        this.corporateName = corporateName;
        this.tradeName = tradeName;
        this.email = email;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressComplement = addressComplement;
        this.district = district;
        this.city = city;
        this.federativeUnit = federativeUnit;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getCompanyRegistry() {
        return companyRegistry;
    }

    public void setCompanyRegistry(String companyRegistry) {
        this.companyRegistry = companyRegistry;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
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

package boxgym.model;

import java.time.LocalDateTime;

public class User {
    private int userId; //Identificador
    private String username; //Nome de usuário
    private String password; //Senha
    private String confirmPassword; //Confirmar Senha
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em
    
    //Construtor CREATE
    public User(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    
    //Construtor SELECT
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

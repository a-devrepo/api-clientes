package br.com.advanced.domain.exceptions;

public class EmailJaExistenteException extends RuntimeException{

    public EmailJaExistenteException(String cpf) {
        super("O email já está cadastrado: " + cpf);
    }
}
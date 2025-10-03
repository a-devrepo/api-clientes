package br.com.advanced.domain.exceptions;

public class CpfJaExistenteException extends RuntimeException{

    public CpfJaExistenteException(String cpf) {
        super("O cpf já está cadastrado: " + cpf);
    }
}

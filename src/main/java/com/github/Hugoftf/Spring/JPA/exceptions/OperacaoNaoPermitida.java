package com.github.Hugoftf.Spring.JPA.exceptions;

public class OperacaoNaoPermitida extends  RuntimeException{

    public OperacaoNaoPermitida(String msg){
        super(msg);
    }

}

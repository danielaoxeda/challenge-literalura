package com.aluracursos.Literalura.model;

public enum Language {

    EN("en"),
    ES("es"),
    FR("fr"),
    PT("pt");

    private String languageAPI;

    Language(String languageAPI){
        this.languageAPI = languageAPI;
    }

    public static Language fromString(String text){
        for(Language l : Language.values()){
            if(l.languageAPI.equalsIgnoreCase(text)){
                return l;
            }
        }
        throw new IllegalArgumentException("Idioma no encontrado");
    }
}
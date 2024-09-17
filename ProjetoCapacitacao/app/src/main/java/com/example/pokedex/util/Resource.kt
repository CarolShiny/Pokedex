package com.example.pokedex.util

//classe com tratamento de erros para serem utilizadas quando precisar
//classe genérica para tratamento de erros
sealed class Resource<T>(val data: T? = null, val message: String? = null ) {
    class Success<T> (data: T) : Resource<T>(data) //imprime a lista normalmente
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message) //usuário desconectado a internet

    class Loading<T>(data: T? = null) : Resource<T>(data) //Erro no carregamento da página
}
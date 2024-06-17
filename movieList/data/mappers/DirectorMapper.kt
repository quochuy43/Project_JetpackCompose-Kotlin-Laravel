package com.example.project_appmovie.movieList.data.mappers

import com.example.project_appmovie.movieList.data.local.director.DirectorEntity
import com.example.project_appmovie.movieList.data.remote.respond.DirectorDTO
import com.example.project_appmovie.movieList.domain.model.Director


fun DirectorDTO.toDirectorEntity(): DirectorEntity {
    return DirectorEntity(
        id = id,
        age = age,
        name = name,
        nationality = nationality,
        information = information,
        profile_image_url = profile_image_url,
        famous_movies = famous_movies,
        awards = awards,
        link_wiki = link_wiki
    )
}

fun DirectorEntity.toDirector(): Director {
    return Director(
        id = id,
        age = age,
        name = name,
        nationality = nationality,
        information = information,
        profile_image_url = profile_image_url,
        famous_movies = famous_movies,
        awards = awards,
        link_wiki = link_wiki
    )
}

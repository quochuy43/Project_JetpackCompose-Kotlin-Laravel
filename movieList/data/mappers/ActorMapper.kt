package com.example.project_appmovie.movieList.data.mappers

import com.example.project_appmovie.movieList.data.local.actor.ActorEntity
import com.example.project_appmovie.movieList.data.remote.respond.ActorDTO
import com.example.project_appmovie.movieList.domain.model.Actor

fun ActorDTO.toActorEntity(): ActorEntity {
    return ActorEntity(
        id = id,
        age = age,
        name = name,
        nationality = nationality,
        information = information,
        profile_image_url = profile_image_url,
        famous_movies = famous_movies,
        awards = awards,
        link_ig = link_ig
    )
}

fun ActorEntity.toActor(): Actor {
    return Actor(
        id = id,
        age = age,
        name = name,
        nationality = nationality,
        information = information,
        profile_image_url = profile_image_url,
        famous_movies = famous_movies,
        awards = awards,
        link_ig = link_ig
    )
}

//fun Actor.toActorEntity(): ActorEntity {
//    return ActorEntity(
//        id = id,
//        age = age,
//        name = name,
//        nationality = nationality,
//        information = information,
//        profile_image_url = profile_image_url,
//        famous_movies = famous_movies,
//        awards = awards
//    )
//}
package com.example.hataru.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class ExampleJson2KtKotlin(
    @JsonProperty("frontend_version") var frontendVersion: String? = null,
    @JsonProperty("roomtypes") var roomtypes: ArrayList<Roomtypes> = arrayListOf()
)

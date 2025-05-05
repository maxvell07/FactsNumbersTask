package com.example.factsnumberstask.numbers.data


interface NumbersCloudDataSource : FetchNumber {

     suspend fun randomNumber(): NumberData
}
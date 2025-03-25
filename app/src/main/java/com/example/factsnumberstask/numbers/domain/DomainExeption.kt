package com.example.factsnumberstask.numbers.domain

abstract class DomainException : IllegalStateException()

class NoInternetConnectionExeption: DomainException()

class ServiceUnavailableException: DomainException()

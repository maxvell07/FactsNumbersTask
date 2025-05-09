package com.example.factsnumberstask.numbers.data.cache

import com.example.factsnumberstask.numbers.data.NumberData

class NumberDataToCache: NumberData.Mapper<NumberCache> {
    override fun map(id: String, fact: String) =  NumberCache(id,fact, System.currentTimeMillis())
}
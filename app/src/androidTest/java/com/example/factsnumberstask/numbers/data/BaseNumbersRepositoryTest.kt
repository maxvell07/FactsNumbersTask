package com.example.factsnumberstask.numbers.data


import com.example.factsnumberstask.numbers.data.cache.NumbersCacheDataSource
import com.example.factsnumberstask.numbers.data.cloud.NumbersCloudDataSource
import com.example.factsnumberstask.numbers.domain.NoInternetConnectionExeption
import com.example.factsnumberstask.numbers.domain.NumberFact
import com.example.factsnumberstask.numbers.domain.NumbersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class BaseNumbersRepositoryTest {

    private lateinit var repository: NumbersRepository
    private lateinit var cloudDataSource: TestNumbersCloudDataSource
    private lateinit var cacheDataSource: TestNumbersCacheDataSource

    @Before
    fun setUp() {
        cloudDataSource = TestNumbersCloudDataSource()
        cacheDataSource = TestNumbersCacheDataSource()
        val mapper = NumberDataToDomain()
        repository = BaseNumbersRepository(
            cloudDataSource,
            cacheDataSource,
            HandleDataRequest.Base(
                cacheDataSource, mapper,
                HandleDomainError()
            ),
            mapper
        )
    }

    @Test
    fun test_all_numbers() = runBlocking {
        cacheDataSource.replaceData(
            listOf(
                NumberData("4", "fact of 4"),
                NumberData("5", "fact of 5"),
            )
        )
        val actual = repository.allNumbers()
        val expected = listOf(
            NumberFact("4", "fact of 4"),
            NumberFact("5", "fact of 5"),
        )
        actual.forEachIndexed { index, item ->
            assertEquals(expected[index], item)
        }
        assertEquals(1, cacheDataSource.allNumbersCalledCount)
    }

    @Test
    fun test_number_fact_not_cached_success() = runBlocking {
        cloudDataSource.makeExpected(NumberData("10", "fact about 10"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.numberFact("10")
        val expected = NumberFact("10", "fact about 10")

        assertEquals(expected, actual)
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(NumberData("10", "fact about 10"), cacheDataSource.data[0])
    }

    @Test(expected = NoInternetConnectionExeption::class)
    fun test_number_fact_not_cached_failure() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.numberFact("10")
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_number_fact_cached() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumberData("10", "cloud 10"))
        cacheDataSource.replaceData(listOf(NumberData("10", "fact 10")))

        val actual = repository.numberFact("10")
        val expected = NumberFact("10", "fact 10")

        assertEquals(expected, actual)
        assertEquals(true, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cacheDataSource.numberFactCalled.size)
        assertEquals("10", cacheDataSource.numberFactCalled[0])
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_random_number_fact_not_cached_success() = runBlocking {
        cloudDataSource.makeExpected(NumberData("10", "fact about 10"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.randomNumberFact()
        val expected = NumberFact("10", "fact about 10")

        assertEquals(expected, actual)


        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(NumberData("10", "fact about 10"), cacheDataSource.data[0])
    }

    @Test(expected = NoInternetConnectionExeption::class)
    fun test_random_number_fact_not_cached_failure() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.randomNumberFact()


        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_random_number_fact_cached() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumberData("10", "cloud 10"))
        cacheDataSource.replaceData(listOf(NumberData("10", "fact 10")))

        val actual = repository.randomNumberFact()
        val expected = NumberFact("10", "cloud 10")

        assertEquals(expected, actual)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)




        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }

    private class TestNumbersCloudDataSource : NumbersCloudDataSource {

        private var thereIsConnection = true
        private var numberData = NumberData("", "")
        var numberFactCalledCount = 0
        var randomNumberFactCalledCount = 0

        fun changeConnection(connected: Boolean) {
            thereIsConnection = connected
        }

        fun makeExpected(number: NumberData) {
            numberData = number
        }

        override suspend fun number(number: String): NumberData {
            numberFactCalledCount++
            return if (thereIsConnection)
                numberData
            else
                throw UnknownHostException()
        }

        override suspend fun randomNumber(): NumberData {
            randomNumberFactCalledCount++
            return if (thereIsConnection)
                numberData
            else
                throw UnknownHostException()
        }
    }

    private class TestNumbersCacheDataSource : NumbersCacheDataSource {

        val containsCalledList = mutableListOf<Boolean>()
        var numberFactCalled = mutableListOf<String>()
        var allNumbersCalledCount = 0
        var saveNumberFactCalledCount = 0

        val data = mutableListOf<NumberData>()

        fun replaceData(newData: List<NumberData>): Unit = with(data) {
            clear()
            addAll(newData)
        }

        override suspend fun allNumbers(): List<NumberData> {
            allNumbersCalledCount++
            return data
        }

        override suspend fun contains(number: String): Boolean {
            val result = data.find { it.map(NumberData.Mapper.Matches(number)) } != null
            containsCalledList.add(result)
            return result
        }

        override suspend fun number(number: String): NumberData {
            numberFactCalled.add(number)
            return data[0]
        }

        override suspend fun saveNumber(numberData: NumberData) {
            saveNumberFactCalledCount++
            data.add(numberData)
        }
    }}
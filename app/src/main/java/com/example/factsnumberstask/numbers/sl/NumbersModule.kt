package com.example.factsnumberstask.numbers.sl

import com.example.factsnumberstask.main.sl.Core
import com.example.factsnumberstask.main.sl.Module
import com.example.factsnumberstask.numbers.data.BaseNumbersRepository
import com.example.factsnumberstask.numbers.data.HandleDataRequest
import com.example.factsnumberstask.numbers.data.HandleDomainError
import com.example.factsnumberstask.numbers.data.NumberDataToDomain
import com.example.factsnumberstask.numbers.data.cache.NumberDataToCache
import com.example.factsnumberstask.numbers.data.cache.NumbersCacheDataSource
import com.example.factsnumberstask.numbers.data.cloud.NumbersCloudDataSource
import com.example.factsnumberstask.numbers.data.cloud.NumbersService
import com.example.factsnumberstask.numbers.domain.HandleError
import com.example.factsnumberstask.numbers.domain.HandleRequest
import com.example.factsnumberstask.numbers.domain.NumberUiMapper
import com.example.factsnumberstask.numbers.domain.NumbersInteractor
import com.example.factsnumberstask.numbers.presentation.HandleNumbersRequest
import com.example.factsnumberstask.numbers.presentation.NumbersCommunications
import com.example.factsnumberstask.numbers.presentation.NumbersListCommunication
import com.example.factsnumberstask.numbers.presentation.NumbersResultMapper
import com.example.factsnumberstask.numbers.presentation.NumbersStateCommunication
import com.example.factsnumberstask.numbers.presentation.NumbersViewModel
import com.example.factsnumberstask.numbers.presentation.ProgressCommunication

class NumbersModule(private val core: Core) : Module<NumbersViewModel> {

    override fun viewModel(): NumbersViewModel {
        val communications = NumbersCommunications.Base(
            ProgressCommunication.Base(),
            NumbersStateCommunication.Base(),
            NumbersListCommunication.Base()
        )
        val cacheDataSource = NumbersCacheDataSource.Base(
            core.provideDataBase().numbersDao(),
            NumberDataToCache()
        )
        val repository = BaseNumbersRepository(
            NumbersCloudDataSource.Base(
                core.service(NumbersService::class.java)
            ),
            cacheDataSource,
            HandleDataRequest.Base(
                cacheDataSource,
                NumberDataToDomain(),
                HandleDomainError()
            ),
            NumberDataToDomain()
        )
        return NumbersViewModel(
            HandleNumbersRequest.Base(
                core.provideDispatchers(),
                communications,
                NumbersResultMapper(communications, NumberUiMapper())
            ),
            core,
            communications,
            NumbersInteractor.Base(
                repository,
                HandleRequest.Base(
                    HandleError.Base(core),
                    repository
                )
            )
        )
    }
}
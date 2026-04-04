package com.alejandra.amordepelis.features.lists.domain.usecases

data class ListsUseCases(
    val getSharedLists: GetSharedListsUseCase,
    val getSharedListDetails: GetSharedListDetailsUseCase,
    val createSharedList: CreateSharedListUseCase,
    val updateSharedList: UpdateSharedListUseCase,
    val deleteSharedList: DeleteSharedListUseCase
)

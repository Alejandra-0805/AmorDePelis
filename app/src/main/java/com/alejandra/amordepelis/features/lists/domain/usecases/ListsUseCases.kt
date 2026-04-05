package com.alejandra.amordepelis.features.lists.domain.usecases

import javax.inject.Inject

data class ListsUseCases @Inject constructor(
    val getSharedLists: GetSharedListsUseCase,
    val getSharedListDetails: GetSharedListDetailsUseCase,
    val createSharedList: CreateSharedListUseCase,
    val updateSharedList: UpdateSharedListUseCase,
    val deleteSharedList: DeleteSharedListUseCase,
    val getAnnouncements: GetAnnouncementsUseCase
)

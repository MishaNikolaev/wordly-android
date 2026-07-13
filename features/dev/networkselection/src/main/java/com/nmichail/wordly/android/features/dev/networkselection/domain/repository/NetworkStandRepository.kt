package com.nmichail.wordly.android.features.dev.networkselection.domain.repository

import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

interface NetworkStandRepository {

	fun getStands(): List<NetworkStand>

	fun getSelected(): NetworkStand

	fun setSelected(stand: NetworkStand)
}

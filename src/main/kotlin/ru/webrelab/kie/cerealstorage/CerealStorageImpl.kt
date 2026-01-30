package ru.webrelab.kie.cerealstorage

import kotlin.IllegalStateException
import kotlin.math.floor


class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        if (amount < 0) throw IllegalArgumentException("Количество не может быть отрицательным")

        val currentAmount = storage[cereal] ?: 0f
        val spaceInContainer = containerCapacity - currentAmount

        val amountContainerInStorage = floor(storageCapacity/containerCapacity)
        val containerCount = storage.keys.size
        if (containerCount >= amountContainerInStorage) {
            throw IllegalStateException("Нет места в хранилище для нового контейнера")
        }

        val containerExists = storage.containsKey(cereal)
        if (!containerExists && currentAmount == 0f) {
            storage[cereal] = 0f
        }

        val canAdd = if (amount <= spaceInContainer) amount else spaceInContainer
        val newAmount = currentAmount + canAdd
        storage[cereal] = newAmount

        return amount - canAdd
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        if (amount < 0) throw IllegalArgumentException("Количество не может быть отрицательным")
        val currentAmount = storage[cereal] ?: 0f
        val amountToTake = if (amount <= currentAmount) amount else 0f
        storage[cereal] = currentAmount - amountToTake
        return if (amount <= currentAmount) amountToTake else currentAmount
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage[cereal]
        if (currentAmount == null || currentAmount != 0f) {
            return false
        }
        storage.remove(cereal)
        return true
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage[cereal] ?: throw IllegalStateException("проверяемого контейнера нет")
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("CerealStorage:\n")
        for ((cereal, amount) in storage) {
            sb.append("${cereal.local}: $amount / $containerCapacity\n")
        }
        return sb.toString()

    }
}
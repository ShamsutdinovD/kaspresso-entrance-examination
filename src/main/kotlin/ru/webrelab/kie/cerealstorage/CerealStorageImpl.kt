package ru.webrelab.kie.cerealstorage


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
        val currentContainerAmount = currentAmount

        val spaceLeftInContainer = containerCapacity - currentContainerAmount
        val totalAvailableSpace = getSpace(cereal)

        val amountToAdd = if (amount <= spaceLeftInContainer) {
            amount
        } else {
            spaceLeftInContainer
        }

        val newAmount = currentAmount + amountToAdd
        val totalStorageUsed = storage.values.sum()
        if (totalStorageUsed + amountToAdd > storageCapacity) {
            val availableStorageSpace = storageCapacity - totalStorageUsed
            if (availableStorageSpace <= 0) {
                throw IllegalStateException("Недостаточно места в хранилище")
            }
            val actualAdd = if (amountToAdd > availableStorageSpace) {
                availableStorageSpace
            } else {
                amountToAdd
            }
            storage[cereal] = currentAmount + actualAdd
            return amount - actualAdd
        } else {
            storage[cereal] = newAmount
            return amount - amountToAdd
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        if (amount < 0) throw IllegalArgumentException("Количество не может быть отрицательным")
        val currentAmount = storage[cereal] ?: 0f
        val amountToTake = if (amount <= currentAmount) amount else currentAmount
        storage[cereal] = currentAmount - amountToTake
        if (storage[cereal] == 0f) {
            storage.remove(cereal)
        }
        return amountToTake
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage[cereal] ?: return false
            storage.remove(cereal)
            return true
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage[cereal] ?: 0f
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
package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)


    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should add cereal when enough space in container and storage`() {
        val remaining = storage.addCereal(Cereal.RICE, 5f)
        assertEquals(0f, remaining)
        assertEquals(5f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `should add cereal to fill container and reduce remaining`() {
        storage.addCereal(Cereal.PEAS, 8f)
        val remaining = storage.addCereal(Cereal.PEAS, 5f)
        assertEquals(3f, remaining)
        assertEquals(10f, storage.getAmount(Cereal.PEAS))
    }

    @Test
    fun `should throw if adding negative amount`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.BULGUR, -1f)
        }
    }

    @Test
    fun `should throw if no space in storage`() {
        storage.addCereal(Cereal.BULGUR, 10f)
        storage.addCereal(Cereal.PEAS, 10f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, 1f)
        }
    }

    @Test
    fun `should get correct amount after multiple additions`() {
        storage.addCereal(Cereal.RICE, 7f)
        storage.addCereal(Cereal.RICE, 2f)
        assertEquals(9f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `getAmount returns 0 if no cereal`() {
        assertEquals(0f, storage.getAmount(Cereal.PEAS))
    }

    @Test
    fun `getSpace returns correct available space`() {
        storage.addCereal(Cereal.BULGUR, 4f)
        assertEquals(6f, storage.getSpace(Cereal.BULGUR))
    }

    @Test
    fun `getSpace returns container capacity if no cereal`() {
        assertEquals(10f, storage.getSpace(Cereal.PEAS))
    }

    @Test
    fun `removeContainer should remove container and update storage`() {
        storage.addCereal(Cereal.MILLET, 5f)
        storage.removeContainer(Cereal.MILLET)
        assertEquals(0f, storage.getAmount(Cereal.MILLET))
    }

}



package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.webrelab.kie.cerealstorage.Cereal.PEAS


class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)


    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should be discarded if the storage capacity is less than the container`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f, 7f)
        }
    }

    @Test
    fun `getCereal returns the correct amount or remainder of the container`() {
        storage.addCereal(Cereal.RICE, 10f)
        val taken = storage.getCereal(Cereal.RICE, 4f)
        assertEquals(6f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should discarded if a negative quantity is requested`() {
        storage.addCereal(Cereal.RICE, 5f)
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.RICE, -1f)
        }
    }

    @Test
    fun `if more is requested than is available in the container`() {
        storage.addCereal(PEAS, 3f)
        val taken = storage.getCereal(PEAS, 5f)
        assertEquals(3f, storage.getAmount(PEAS), 0.01f)
    }

    @Test
    fun `removeContainer returns false if the container is not empty`() {
        storage.addCereal(Cereal.MILLET, 5f)
        storage.removeContainer(Cereal.MILLET)
        assertFalse(storage.removeContainer(Cereal.MILLET))
    }

    @Test
    fun `removeContainer should remove the empty container`() {
        storage.addCereal(Cereal.MILLET, 0f)
        assertTrue(storage.removeContainer(Cereal.MILLET))
    }

    @Test
    fun `getAmount returns 0 if no cereal`() {
        assertEquals(0f, storage.getAmount(PEAS))
    }

    @Test
    fun `getAmount returns a value if the cereal is present`() {
        storage.addCereal(Cereal.PEAS, 5f)
        assertEquals(5f, storage.getAmount(PEAS))
    }

    @Test
    fun `getSpace returns correct available space`() {
    storage.addCereal(Cereal.BULGUR, 4f)
    assertEquals(6f, storage.getSpace(Cereal.BULGUR), 0.01f)
    }

    @Test
    fun `getSpace returns container capacity if no cereal`() {
        assertThrows(IllegalStateException::class.java) {
            val cereal = Cereal.PEAS
            storage.getSpace(Cereal.PEAS)
        }
    }

    @Test
    fun `should add cereal when enough space in container and storage`() {
        storage.addCereal(Cereal.RICE, 5f)
        assertEquals(5f, storage.getAmount(Cereal.RICE))
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
        storage.addCereal(PEAS, 10f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, 1f)
        }
    }

    @Test
    fun `should add cereal to fill container and reduce remaining`() {
        storage.addCereal(PEAS, 8f)
        val remaining = storage.addCereal(PEAS, 5f)
        assertEquals(3f, remaining)
        assertEquals(10f, storage.getAmount(PEAS))
    }

    @Test
    fun `should get correct amount after multiple additions`() {
        storage.addCereal(Cereal.RICE, 7f)
        storage.addCereal(Cereal.RICE, 2f)
        assertEquals(9f, storage.getAmount(Cereal.RICE))
    }
}
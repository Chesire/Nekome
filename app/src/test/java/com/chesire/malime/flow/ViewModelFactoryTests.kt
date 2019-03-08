package com.chesire.malime.flow

import androidx.lifecycle.ViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test
import javax.inject.Provider

class ViewModelFactoryTests {
    @Test
    fun `create with stored ViewModel returns it`() {
        val mockProvider = mockk<Provider<ViewModel>> {
            every { get() } returns mockk()
        }
        val input = mapOf<Class<out ViewModel>, Provider<ViewModel>>(
            ViewModel1::class.java to mockProvider
        )
        val classUnderTest = ViewModelFactory(input)

        assertNotNull(classUnderTest.create(ViewModel1::class.java))
    }

    @Test
    fun `create without stored ViewModel returns first matching`() {
        val mockProvider = mockk<Provider<ViewModel>> {
            every { get() } returns mockk()
        }
        val input = mapOf<Class<out ViewModel>, Provider<ViewModel>>(
            ViewModel2::class.java to mockProvider
        )
        val classUnderTest = ViewModelFactory(input)

        assertNotNull(classUnderTest.create(ViewModel1::class.java))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create without stored ViewModel and no matching throws IllegalArgumentException`() {
        val input = mapOf<Class<out ViewModel>, Provider<ViewModel>>()
        val classUnderTest = ViewModelFactory(input)

        classUnderTest.create(ViewModel1::class.java)
        fail("Exception should have occurred")
    }

    private open class ViewModel1 : ViewModel()
    private open class ViewModel2 : ViewModel1()
}

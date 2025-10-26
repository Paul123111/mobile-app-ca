package ie.setu.appstore

import ie.setu.appstore.models.AppModel
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class AppModelTest {
    lateinit var app: AppModel
    @Before
    fun setUp() {
        app = AppModel()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addRating() {
        app.addRating(1)
        assertEquals(app.ratings.get(0), 1)
    }

    @Test
    fun avgRating() {
        assertEquals(app.avgRating(), 3F)
        app.addRating(1)
        app.addRating(2)
        app.addRating(3)
        assertEquals(app.avgRating(), 2F)
    }
}
package com.example.albertsonscodingchallenge

import com.example.albertsonscodingchallenge.util.ImageListConverter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ImageListConverterTest {

    private val imageListConverter = ImageListConverter()

    @Test
    fun `fromString with null input should return an empty list`() {
        val result = imageListConverter.fromString(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `fromString with valid input should return the correct list`() {
        val input = """["url1", "url2", "url3"]"""
        val result = imageListConverter.fromString(input)
        assertEquals(listOf("url1", "url2", "url3"), result)
    }

    @Test
    fun `fromList should convert a list of strings to a JSON string`() {
        val inputList = listOf("url1", "url2", "url3")
        val result = imageListConverter.fromList(inputList)
        val expectedJson = """["url1","url2","url3"]"""
        assertEquals(expectedJson, result)
    }
}

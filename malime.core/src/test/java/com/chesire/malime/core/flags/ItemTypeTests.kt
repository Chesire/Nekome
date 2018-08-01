package com.chesire.malime.core.flags

import org.junit.Assert
import org.junit.Test

class ItemTypeTests {
    @Test
    fun `get type for "unknown", returns Unknown itemtype`() {
        Assert.assertEquals(ItemType.Unknown, ItemType.getTypeForString("unknown"))
    }

    @Test
    fun `get type for "anime", returns Anime itemtype`() {
        Assert.assertEquals(ItemType.Anime, ItemType.getTypeForString("anime"))
    }

    @Test
    fun `get type for "manga", returns Manga itemtype`() {
        Assert.assertEquals(ItemType.Manga, ItemType.getTypeForString("manga"))
    }

    @Test
    fun `get type for "unexpected", returns Unknown itemtype`() {
        Assert.assertEquals(ItemType.Unknown, ItemType.getTypeForString("unexpected"))
    }

    @Test
    fun `get type for id 0, returns Unknown itemtype`() {
        Assert.assertEquals(ItemType.Unknown, ItemType.getTypeForInternalId(0))
    }

    @Test
    fun `get type for id 1, returns Anime itemtype`() {
        Assert.assertEquals(ItemType.Anime, ItemType.getTypeForInternalId(1))
    }

    @Test
    fun `get type for id 2, returns Manga itemtype`() {
        Assert.assertEquals(ItemType.Manga, ItemType.getTypeForInternalId(2))
    }

    @Test
    fun `get type for id 999, returns Unknown itemtype`() {
        Assert.assertEquals(ItemType.Unknown, ItemType.getTypeForInternalId(999))
    }
}
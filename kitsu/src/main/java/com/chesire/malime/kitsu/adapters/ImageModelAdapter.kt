package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.kitsu.api.intermediaries.ParsingImageModel
import com.squareup.moshi.FromJson

class ImageModelAdapter {
    @FromJson
    fun imageFromParsingImage(parsingImage: ParsingImageModel): ImageModel {
        return ImageModel(
            tiny = constructImageData(parsingImage.tiny, parsingImage.meta.dimensions.tiny),
            small = constructImageData(parsingImage.small, parsingImage.meta.dimensions.small),
            medium = constructImageData(parsingImage.medium, parsingImage.meta.dimensions.medium),
            large = constructImageData(parsingImage.large, parsingImage.meta.dimensions.large)
        )
    }

    private fun constructImageData(
        sizeUrl: String,
        dimensionsData: ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData?
    ) = if (sizeUrl.isEmpty()) {
        ImageModel.ImageData.empty
    } else {
        ImageModel.ImageData(sizeUrl, dimensionsData?.width ?: 0, dimensionsData?.height ?: 0)
    }
}

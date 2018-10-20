package com.chesire.malime

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class AllOpen

@AllOpen
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting
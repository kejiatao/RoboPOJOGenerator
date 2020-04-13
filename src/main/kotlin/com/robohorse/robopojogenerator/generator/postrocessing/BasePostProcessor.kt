package com.robohorse.robopojogenerator.generator.postrocessing

import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.common.ClassItem
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.generator.utils.ClassTemplateHelper
import com.robohorse.robopojogenerator.models.GenerationModel

abstract class BasePostProcessor(
        protected val generateHelper: ClassGenerateHelper,
        protected val classTemplateHelper: ClassTemplateHelper
) {

    fun proceed(
            classItem: ClassItem,
            generationModel: GenerationModel
    ): String {
        applyAnnotations(generationModel.annotationEnum, classItem)
        return proceedClass(classItem, generationModel)
    }

    protected abstract fun applyAnnotations(item: AnnotationEnum, classItem: ClassItem)

    abstract fun proceedClassBody(classItem: ClassItem, generationModel: GenerationModel): String?

    abstract fun createClassTemplate(classItem: ClassItem, classBody: String?): String

    private fun proceedClass(
            classItem: ClassItem,
            generationModel: GenerationModel
    ): String {
        val classBody = generateHelper.updateClassBody(
                proceedClassBody(classItem, generationModel)
        )
        val classTemplate = createClassTemplate(classItem, classBody)
        val importsBuilder = proceedClassImports(classItem)
        return createClassItemText(
                classItem.packagePath,
                importsBuilder.toString(),
                classTemplate
        )
    }

    protected open fun proceedClassImports(classItem: ClassItem): StringBuilder {
        val imports = classItem.classImports
        val importsBuilder = StringBuilder()
        for (importItem in imports) {
            importsBuilder.append(importItem)
            importsBuilder.append(ClassTemplate.NEW_LINE)
        }
        return importsBuilder
    }

    protected open fun createClassItemText(
            packagePath: String?,
            imports: String?,
            classTemplate: String?
    ) = classTemplateHelper.createClassItem(
            packagePath,
            imports,
            classTemplate
    )
}
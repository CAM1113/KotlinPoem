package com.cam.ktl

import com.squareup.kotlinpoet.*
import java.io.File

private const val packageName = "com.cam.ktl"
private const val fileName = "KotlinPoem"
private val stringClassName = ClassName("kotlin", "String")
private val intClassName = ClassName("kotlin", "Int")


//class PoemMaker
fun getPoemMakerClass(): TypeSpec {
    val poemMakerConstructor = FunSpec.constructorBuilder()
        .addParameter("name", stringClassName)
        .addParameter("age", intClassName)
        .build()

    return TypeSpec.classBuilder("PoemMaker")
        .primaryConstructor(poemMakerConstructor)
        .addProperty(PropertySpec.builder("name", stringClassName).initializer("name").build())
        .addProperty(PropertySpec.builder("age", intClassName).initializer("age").build())
        .build()
}

//class Poem
fun getPoemClass(): TypeSpec {
    val poemMakerConstructor = FunSpec.constructorBuilder()
        .addParameter("title", stringClassName)
        .addParameter("content", stringClassName)
        .build()

    return TypeSpec.classBuilder("Poem")
        .primaryConstructor(poemMakerConstructor)
        .addProperty(PropertySpec.builder("title", stringClassName).initializer("title").build())
        .addProperty(PropertySpec.builder("content", stringClassName).initializer("content").build())
        .build()
}

//interface PoemPrinter
fun getPoemPrinterInterface(): TypeSpec {
    val printFun = FunSpec.builder("printPoem")
        .returns(intClassName)
        .addModifiers(KModifier.ABSTRACT)
        .build()
    return TypeSpec.interfaceBuilder("PoemPrinter")
        .addFunction(printFun)
        .build()
}

//class KotlinPoem
fun getKotlinPoemClass(): TypeSpec {
    val primaryConstructor = FunSpec.constructorBuilder()
        .addModifiers(KModifier.PRIVATE)
        .build()

    val poemMakerClazzName = ClassName(packageName, "PoemMaker")
    val makerParameterName = ParameterSpec.builder("maker", poemMakerClazzName)
        .defaultValue("PoemMaker(%S, 0)", "")
        .build()

    val poemClazzName = ClassName(packageName, "Poem")
    val poemParameterName = ParameterSpec.builder("poem", poemClazzName)
        .defaultValue("Poem(%S, %S)", "", "")
        .build()

    val secondConstructor = FunSpec.constructorBuilder()
        .addModifiers(KModifier.PUBLIC)
        .addParameter(makerParameterName)
        .addParameter(poemParameterName)
        .callThisConstructor()
        .addCode(
            """
            this.maker = maker
            this.poem = poem
            """.trimIndent()
        )
        .build()

    val printFunc = FunSpec.builder("printPoem")
        .returns(intClassName)
        .addStatement(
            """
            var wordNum = 0
        val title = poem.title
        wordNum += title.length
        println(title)
        val authorInfo = maker.name +" "+ maker.age
        wordNum += authorInfo.length
        println(authorInfo)
        val content = poem.content
        wordNum += content.length
        println(content)
        return wordNum
            """.trimIndent()
        )
        .addModifiers(KModifier.OVERRIDE)
        .build()

    val poemPrinterInterface = ClassName(packageName, "PoemPrinter")
    return TypeSpec.classBuilder("KotlinPoem")
        .primaryConstructor(primaryConstructor)
        .addSuperinterface(poemPrinterInterface)
        .addFunction(secondConstructor)
        .addProperty(
            PropertySpec.builder("maker", poemMakerClazzName).addModifiers(KModifier.LATEINIT)
                .mutable(true)
                .build()
        )
        .addProperty(
            PropertySpec.builder("poem", poemClazzName).addModifiers(KModifier.LATEINIT)
                .mutable(true)
                .build()
        )
        .addFunction(printFunc)
        .build()
}

//方法 main
fun getMainFun(): FunSpec {
    return FunSpec.builder("main")
        .addStatement(
            """
val poemStr = ""${'"'}nothing is all you need""${'"'}
    val poem = KotlinPoem(
        maker = PoemMaker("CAM", 25),
        poem = Poem(
            title = "nothing", content = poemStr
        )
    )
    val wordNum = poem.printPoem()
    println("====We have print ${"\$"}wordNum Characters ====")
        """.trimIndent()
        )
        .build()
}

private fun write(fileSpec: FileSpec) {
    val f = File("./temp")
    fileSpec.writeTo(f)
}

fun main() {
    val fileSpec = FileSpec.builder(packageName, fileName)
        .addType(getPoemMakerClass())
        .addType(getPoemClass())
        .addType(getPoemPrinterInterface())
        .addType(getKotlinPoemClass())
        .addFunction(getMainFun())
        .build()
    write(fileSpec)
}
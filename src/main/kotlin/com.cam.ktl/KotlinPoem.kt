package com.cam.ktl

import kotlin.Int
import kotlin.String
import kotlin.Unit

public class PoemMaker(
  public val name: String,
  public val age: Int
)

public class Poem(
  public val title: String,
  public val content: String
)

public interface PoemPrinter {
  public fun printPoem(): Int
}

public class KotlinPoem private constructor() : PoemPrinter {
  public lateinit var maker: PoemMaker

  public lateinit var poem: Poem

  public constructor(maker: PoemMaker = PoemMaker("", 0), poem: Poem = Poem("", "")) : this() {
    this.maker = maker
    this.poem = poem
  }

  public override fun printPoem(): Int {
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
  }
}

public fun main(): Unit {
              
      val poemStr = """nothing is all you need
because you are nothing
      nothing is all we need
      because everything is nothing"""
          val poem = KotlinPoem(
              maker = PoemMaker("CAM", 25),
              poem = Poem(
                  title = "nothing", content = poemStr
              )
          )
          val wordNum = poem.printPoem()
          println("====We have print $wordNum Characters ====")
}

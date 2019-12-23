package dev.mee42

import org.w3c.dom.*
import kotlin.browser.document

fun indexEntry(){
    submitButton.onclick = {
        textEntry.value
            .takeUnless { it.isBlank() }
            ?.let { content ->
                addNewMessage(
                    author = "Arson#yes",
                    content = content
                )
            }
    }
}

fun addNewMessage(author: String, content: String) {
    val newLi = elem<HTMLUListElement>("li") {
        appendChild(elem<HTMLDivElement>("div") {
            classList.add("message")
            appendChild(elem<HTMLParagraphElement>("p") {
                classList.add("message-author")
                innerText = author
            })
            appendChild(elem<HTMLParagraphElement>("p") {
                classList.add("message-content")
                innerText = content
            })
        })
    }
    messageList.appendChild(newLi)
    console.log(dyn {
        this.author = author
        this.content = content
    } as? Any)
}

@Suppress("UNCHECKED_CAST")
fun <T : HTMLElement> elem(str: String, conf: T.() -> Unit): T {
    val elem = document.createElement(str) as T
    conf(elem)
    return elem
}


private val messageList by lazyId<HTMLUListElement>("message-list")
private val textEntry by lazyId<HTMLTextAreaElement>("text-input")
private val submitButton by lazyId<HTMLButtonElement>("submit-button")